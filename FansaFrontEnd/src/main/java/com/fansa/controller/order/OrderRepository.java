package com.fansa.controller.order;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Modifying
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    public List<Order> findByCustomer(Long customerId);
}
