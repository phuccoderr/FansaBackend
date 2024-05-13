package com.fansa.customer;

import com.fansa.common.entity.Customer;
import com.fansa.controller.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerRepositoryTests {
    @Autowired private CustomerRepository repository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void createCustomer() {
        Customer customer = new Customer();
        customer.setEmail("phuc1@gmail.com");
        String password = "0123456789";
        String encodePassword = encoder.encode(password);
        customer.setPassword(encodePassword);
        customer.setCreatedTime(LocalDate.now());
        customer.setEnabled(true);
        customer.setName("Phuc");

        Customer save = repository.save(customer);

    }

    @Test
    public void findByEmail() {
        String email = "phuc@gmail.com";
        Customer byEmail = repository.findByEmail(email);
        String encodePassword = encoder.encode("0123456789");
        byEmail.setPassword(encodePassword);

        repository.save(byEmail);
        System.out.println(byEmail.getEmail());
    }


}
