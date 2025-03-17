package com.ecommerce.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TopFiveUsersByOrdersResponse {

    private String userId;

    private String name;

    private String quantity;
}