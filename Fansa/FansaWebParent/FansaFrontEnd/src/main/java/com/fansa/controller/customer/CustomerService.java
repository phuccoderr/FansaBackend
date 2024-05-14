package com.fansa.controller.customer;

import com.fansa.common.entity.Customer;
import com.fansa.request.RegisterRequest;
import com.fansa.response.customer.Oauth2FB.Oauth2FB;
import com.fansa.response.customer.Oauth2FB.Oauth2GG;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CustomerService {
    private static final String scope = "openid email profile";
    @Autowired private CustomerRepository repository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public Customer register(RegisterRequest request) throws CustomerInvalidException {
        Customer customerInDB = repository.findByEmail(request.getEmail());

        if (customerInDB != null) {
            throw new CustomerInvalidException("Email already exist!");
        }
        String encodePassword = encoder.encode(request.getPassword());

        Customer customer = Customer.builder()
                .email(request.getEmail())
                .password(encodePassword)
                .name(request.getName())
                .createdTime(LocalDate.now())
                .enabled(true).build();

        return repository.save(customer);
    }

    public Customer saveOauth2GG(Oauth2GG oauth2Info) {
        Customer customerInDB = repository.findByEmail(oauth2Info.getEmail());
        if (customerInDB == null) {
            Customer customer = Customer.builder()
                    .email(oauth2Info.getEmail())
                    .name(oauth2Info.getName())
                    .createdTime(LocalDate.now())
                    .enabled(true)
                    .photo(oauth2Info.getPicture())
                    .build();

            return repository.save(customer);
        }
        return customerInDB;
    }

    public Customer saveOauth2FB(Oauth2FB oauth2Info) {
        Customer customerInDB = repository.findByEmail(oauth2Info.getEmail());
        if (customerInDB == null) {
            Customer customer = Customer.builder()
                    .email(oauth2Info.getEmail())
                    .name(oauth2Info.getName())
                    .createdTime(LocalDate.now())
                    .enabled(true)
                    .photo(oauth2Info.getPicture().getData().getUrl())
                    .build();

            return repository.save(customer);
        }
        return customerInDB;
    }
}
