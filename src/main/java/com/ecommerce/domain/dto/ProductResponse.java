package com.ecommerce.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductResponse {

    private String name;

    private String description;

    private BigDecimal price;

    private String category;
}