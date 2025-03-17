package com.ecommerce.domain.repository;

import com.ecommerce.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> { }