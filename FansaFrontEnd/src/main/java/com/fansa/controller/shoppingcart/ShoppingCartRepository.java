package com.fansa.controller.shoppingcart;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.Product;
import com.fansa.common.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

    public ShoppingCart findByCustomerAndProduct(Customer customer, Product product);
    public List<ShoppingCart> findByCustomer(Customer customer);
}
