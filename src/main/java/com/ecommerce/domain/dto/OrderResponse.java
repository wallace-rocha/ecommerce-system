package com.ecommerce.domain.dto;

import com.ecommerce.domain.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderResponse {

    private String orderId;

    private OrderStatus status;

    private String totalAmount;
}