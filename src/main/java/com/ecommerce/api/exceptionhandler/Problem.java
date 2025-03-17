package com.ecommerce.api.exceptionhandler;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Problem {

    String message;
    LocalDateTime dateTime;

}