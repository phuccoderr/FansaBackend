package com.fansa.customer;

import com.fansa.common.entity.Customer;
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
        customer.setEmail("phuc@gmail.com");
        String password = "0123456789";
        String encodePassword = encoder.encode(password);
        customer.setPassword(encodePassword);
        customer.setCreatedTime(LocalDate.now());
        customer.setEnabled(true);

        Customer save = repository.save(customer);

    }

    @Test
    public void findByEmail() {
        String email = "phuc@gmail.com";
        Customer byEmail = repository.findByEmail(email);
        System.out.println(byEmail.getEmail());
    }


}
