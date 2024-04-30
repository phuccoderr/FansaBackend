package com.fansa.admin.customer;

import com.fansa.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired private CustomerRepository repo;

    public List<Customer> getListAll() {
        return repo.findAll();
    }
}
