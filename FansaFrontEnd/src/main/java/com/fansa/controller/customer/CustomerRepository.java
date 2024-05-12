package com.fansa.controller.customer;

import com.fansa.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    public Customer findByEmail(String email);
}
