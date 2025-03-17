package com.ecommerce.api.controller;

import com.ecommerce.domain.dto.ProductRequest;
import com.ecommerce.domain.dto.ProductResponse;
import com.ecommerce.domain.model.Product;
import com.ecommerce.domain.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest productRequest) {
        logger.info("Received request to create product: {}", productRequest);
        try {
            Product product = productService.createProduct(productRequest);
            logger.info("Product created successfully: {}", product);
            return productService.getProductResponse(product);
        } catch (Exception e) {
            logger.error("Error creating product: {}", productRequest, e);
            throw e;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> findAll() {
        logger.info("Received request to get products.");
        try {
            List<ProductResponse> products = productService.findProducts();
            logger.info("Products get successfully.");
            return products;
        } catch (Exception e) {
            logger.error("Error retrieving products result.", e);
            throw e;
        }
    }

    @GetMapping("{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse findById(@PathVariable String productId) {
        logger.info("Received request to get product: {}", productId);
        try {
            Product product = productService.findById(productId);
            logger.info("Product get successfully: {}", productId);
            return productService.getProductResponse(product);
        } catch (Exception e) {
            logger.error("Error retrieving product result: {}", productId, e);
            throw e;
        }
    }

    @PutMapping("{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse update(@PathVariable String productId, @Valid @RequestBody ProductRequest productRequest) {
        logger.info("Received request to update product: {}", productId);
        try {
            Product productUpdated = productService.updateProduct(productId, productRequest);
            logger.info("Product updated successfully: {}", productRequest);
            return productService.getProductResponse(productUpdated);
        } catch (Exception e) {
            logger.error("Error updating product: {}", productId, e);
            throw e;
        }
    }

    @DeleteMapping("{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String productId) {
        logger.info("Received request to delete product: {}", productId);
        try {
            productService.deleteProduct(productId);
            logger.info("Product delete successfully: {}", productId);
        } catch (Exception e) {
            logger.error("Error deleting product: {}", productId, e);
            throw e;
        }
    }
}