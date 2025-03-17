package com.ecommerce.api.controller;

import com.ecommerce.domain.dto.AverageTicketPerUserResponse;
import com.ecommerce.domain.dto.OrderRequest;
import com.ecommerce.domain.dto.OrderResponse;
import com.ecommerce.domain.dto.TopFiveUsersByOrdersResponse;
import com.ecommerce.domain.model.User;
import com.ecommerce.domain.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@AuthenticationPrincipal User user, @Valid @RequestBody OrderRequest orderRequest) {
        logger.info("Received request to create order: {}", orderRequest);
        try {
            OrderResponse order = orderService.createOrder(user, orderRequest);
            logger.info("Order created successfully: {}", order);
            return order;
        } catch (Exception e) {
            logger.error("Error creating order: {}", orderRequest, e);
            throw e;
        }
    }

    @PatchMapping("/{orderId}/pay")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse payOrder(@PathVariable String orderId) {
        logger.info("Received request to pay order: {}", orderId);
        try {
            OrderResponse order = orderService.payOrder(orderId);
            logger.info("Order paid successfully: {}", order);
            return order;
        } catch (Exception e) {
            logger.error("Error paying for order: {}", orderId, e);
            throw e;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> findOrders(@AuthenticationPrincipal User user) {
        logger.info("Received request to get orders.");
        try {
            List<OrderResponse> orderResponse = orderService.findOrders(user);
            logger.info("Orders get successfully.");
            return orderResponse;
        } catch (Exception e) {
            logger.error("Error retrieving orders result.", e);
            throw e;
        }
    }

    @GetMapping("/top-five-users-by-orders")
    @ResponseStatus(HttpStatus.OK)
    public List<TopFiveUsersByOrdersResponse> findTop5UsersByOrders() {
        logger.info("Received request to get top 5 users by orders.");
        try {
            List<TopFiveUsersByOrdersResponse> top5Users = orderService.findTop5UsersByOrders();
            logger.info("Top 5 users by orders get successfully.");
            return top5Users;
        } catch (Exception e) {
            logger.error("Error retrieving top 5 users by orders result.", e);
            throw e;
        }
    }

    @GetMapping("/find-average-ticket-per-user")
    @ResponseStatus(HttpStatus.OK)
    public List<AverageTicketPerUserResponse> findAverageTicketPerUser() {
        logger.info("Received request to get average ticket per user.");
        try {
            List<AverageTicketPerUserResponse> averageTicketPerUser = orderService.findAverageTicketPerUser();
            logger.info("Average ticket per user get successfully.");
            return averageTicketPerUser;
        } catch (Exception e) {
            logger.error("Error retrieving average ticket per user result.", e);
            throw e;
        }
    }

    @GetMapping("/total-monthly-revenue/{month}")
    @ResponseStatus(HttpStatus.OK)
    public String totalMonthlyRevenue(@PathVariable String month) {
        logger.info("Received request to get total monthly revenue.");
        try {
            String totalMonthlyRevenue = orderService.totalMonthlyRevenue(month);
            logger.info("Total monthly revenue get successfully.");
            return totalMonthlyRevenue;
        } catch (Exception e) {
            logger.error("Error retrieving total monthly revenue result.", e);
            throw e;
        }
    }

}