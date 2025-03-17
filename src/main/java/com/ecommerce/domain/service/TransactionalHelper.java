package com.ecommerce.domain.service;

import com.ecommerce.domain.model.Order;
import com.ecommerce.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalHelper {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrderStatus(Order order) {
        orderRepository.save(order);
    }
}