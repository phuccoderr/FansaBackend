package com.fansa.security;

import com.fansa.common.entity.Customer;
import com.fansa.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {
    @Autowired
    private CustomerRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = repo.findByEmail(email);
        if (customer != null) {
            return new CustomerDetail(customer);
        }
        throw new UsernameNotFoundException("Cloud not find user with Email: " + email);
    }
}
