package com.ecommerce.domain.service;

import com.ecommerce.domain.dto.AverageTicketPerUserResponse;
import com.ecommerce.domain.dto.OrderRequest;
import com.ecommerce.domain.dto.OrderResponse;
import com.ecommerce.domain.dto.TopFiveUsersByOrdersResponse;
import com.ecommerce.domain.exception.EntityNotFoundException;
import com.ecommerce.domain.exception.OrderNotPendingException;
import com.ecommerce.domain.exception.OutOfStockFoundException;
import com.ecommerce.domain.model.*;
import com.ecommerce.domain.repository.OrderItemRepository;
import com.ecommerce.domain.repository.OrderRepository;
import com.ecommerce.domain.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private static final NumberFormat formatCurrency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private TransactionalHelper transactionalHelper;

    @Transactional
    public OrderResponse createOrder(User user, OrderRequest orderRequest) {
        logger.info("Starting order creation: {}", orderRequest);
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemRequest -> {
            Product product = null;
            try {
                product = productService.findById(itemRequest.getProductId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                logger.error("Insufficient stock for product: {}", product.getName());
                try {
                    throw new Exception(String.format("Error processing order. Insufficient stock for product: %s", product.getName()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            return new OrderItem(null, null, product, itemRequest.getQuantity(), product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }).collect(Collectors.toList());

        BigDecimal totalAmount = orderItems.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order(null, user, OrderStatus.PENDENTE, totalAmount, LocalDateTime.now(), orderItems);
        order = orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }

        return new OrderResponse(order.getId(), order.getStatus(), formatCurrency.format(order.getTotalAmount()));
    }

    @Transactional
    public OrderResponse payOrder(String orderId) throws Exception {
        logger.info("Starting pay order creation by Id: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found by ID: {}", orderId);
                    return new Exception(String.format("There is no product registration with a code: %s", orderId));
                });

        if (!order.getStatus().equals(OrderStatus.PENDENTE)) {
            logger.error("Order status is different from pending for ID: {}", orderId);
            throw new Exception(String.format("Payment cannot be processed. Order ID %s has already been paid or canceled.", orderId));
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            if (product.getStockQuantity() < item.getQuantity()) {
                order.setStatus(OrderStatus.CANCELADO);
                transactionalHelper.saveOrderStatus(order);
                logger.error("Order {} canceled due to insufficient stock.", orderId);
                throw new Exception(String.format("Error processing order. Insufficient stock for product: %s", product.getName()));
            }
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.PAGO);
        orderRepository.save(order);

        return new OrderResponse(order.getId(), order.getStatus(), formatCurrency.format(order.getTotalAmount()));
    }

    public List<OrderResponse> findOrders(User user) {
        logger.info("Finding orders for user ID: {}.", user.getId());
        return orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(user.getId()))
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(formatCurrency.format(order.getTotalAmount()))
                .build();
    }

    public List<TopFiveUsersByOrdersResponse> findTop5UsersByOrders() {
        return orderRepository.findTop5UsersByOrders()
                .stream()
                .map(objects -> {
                    return TopFiveUsersByOrdersResponse.builder()
                            .userId(objects[0].toString())
                            .name(objects[1].toString())
                            .quantity(objects[2].toString())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<AverageTicketPerUserResponse> findAverageTicketPerUser() {
        return orderRepository.findAverageTicketPerUser()
                .stream()
                .map(objects -> {
                    return AverageTicketPerUserResponse.builder()
                            .userId(objects[0].toString())
                            .name(objects[1].toString())
                            .average(formatCurrency.format(objects[2]))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public String totalMonthlyRevenue(String month) {
        String formattedMonth = StringUtils.leftPad(month, 2, "0");
        return formatCurrency.format(orderRepository.totalMonthlyRevenue(formattedMonth));
    }
}