package com.sdet.auto.springboot2api.repository;

import com.sdet.auto.springboot2api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}