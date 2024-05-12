package com.fansa.shoppingcart;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.ShoppingCart;
import com.fansa.controller.customer.CustomerRepository;
import com.fansa.controller.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ShoppingcartRepositoryTests {

    @Autowired private ShoppingCartRepository repository;
    @Autowired private CustomerRepository customerRepository;

    @Test
    public void findByCustomer() {
        Customer customer = customerRepository.findById(1L).get();
        List<ShoppingCart> byCustomer = repository.findByCustomer(customer);
        byCustomer.forEach( item -> {
            System.out.println(item.getId());
        });
    }
}
