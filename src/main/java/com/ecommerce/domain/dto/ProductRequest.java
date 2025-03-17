package com.ecommerce.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotNull(message = "Price is required.")
    private BigDecimal price;

    @NotBlank(message = "Category is required.")
    private String category;

    @NotNull(message = "Stock quantity is required.")
    private Integer stockQuantity;
}