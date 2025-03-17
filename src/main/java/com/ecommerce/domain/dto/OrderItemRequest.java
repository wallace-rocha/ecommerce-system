package com.ecommerce.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    @NotBlank(message = "Product id is required.")
    private String productId;

    @NotNull(message = "Quantity is required.")
    private Integer quantity;
}