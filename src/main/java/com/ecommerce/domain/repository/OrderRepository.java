package com.ecommerce.domain.repository;

import com.ecommerce.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o.user.id, o.user.name, COUNT(o.id) FROM Order o WHERE o.status = 'PAGO' GROUP BY o.user.id ORDER BY COUNT(o.id) DESC LIMIT 5")
    List<Object[]> findTop5UsersByOrders();

    @Query("SELECT o.user.id, o.user.name, AVG(o.totalAmount) FROM Order o WHERE o.status = 'PAGO' GROUP BY o.user.id")
    List<Object[]> findAverageTicketPerUser();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'PAGO' AND DATE_FORMAT(o.createdAt, '%m') = :month")
    int totalMonthlyRevenue(String month);
}