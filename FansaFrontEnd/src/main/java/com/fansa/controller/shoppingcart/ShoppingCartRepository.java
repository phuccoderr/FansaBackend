package com.fansa.controller.shoppingcart;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.Product;
import com.fansa.common.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

    public ShoppingCart findByCustomerAndProduct(Customer customer, Product product);
    public List<ShoppingCart> findByCustomer(Customer customer);

    @Modifying
    @Query("DELETE ShoppingCart c WHERE c.customer.id = :customerId")
    public void deleteAllByCustomer(Long customerId);
}
