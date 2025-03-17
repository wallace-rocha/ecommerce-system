package com.ecommerce.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class AverageTicketPerUserResponse {

    private String userId;

    private String name;

    private String average;
}