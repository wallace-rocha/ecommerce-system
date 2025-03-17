package com.ecommerce.domain.service;

import com.ecommerce.domain.dto.ProductRequest;
import com.ecommerce.domain.dto.ProductResponse;
import com.ecommerce.domain.exception.BusinessException;
import com.ecommerce.domain.exception.EntityNotFoundException;
import com.ecommerce.domain.exception.ProductNotFoundException;
import com.ecommerce.domain.model.Product;
import com.ecommerce.domain.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product createProduct(ProductRequest productRequest) {
        logger.info("Starting product creation: {}", productRequest);
        try {
            Product product = Product.builder()
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .category(productRequest.getCategory())
                    .stockQuantity(productRequest.getStockQuantity())
                    .createdAt(LocalDateTime.now())
                    .build();

            return productRepository.save(product);
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public List<ProductResponse> findProducts() {
        logger.info("Finding products.");
        return productRepository.findAll().stream()
                .map(product -> {
                    return ProductResponse.builder()
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                            .category(product.getCategory())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Product findById(String productId) throws Exception {
        logger.info("Finding product by ID: {}", productId);
        return productRepository.findById(productId).orElseThrow(() -> {
            logger.error("Product not found for ID: {}", productId);
            return new Exception(String.format("There is no product registration with a code: %s", productId));
        });
    }

    public ProductResponse getProductResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .build();
    }

    public Product updateProduct(String productId, ProductRequest productRequest) {
        logger.info("Starting product update: {}", productRequest);
        try {
            Product product = findById(productId);
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setCategory(productRequest.getCategory());
            product.setStockQuantity(productRequest.getStockQuantity());
            product.setUpdatedAt(LocalDateTime.now());

            return productRepository.save(product);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public void deleteProduct(String productId) throws Exception {
        findById(productId);
        productRepository.deleteById(productId);
    }
}