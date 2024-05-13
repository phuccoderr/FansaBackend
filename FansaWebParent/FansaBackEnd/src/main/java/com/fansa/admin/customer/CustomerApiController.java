package com.fansa.admin.customer;

import com.fansa.admin.customer.request.CustomerDTO;
import com.fansa.common.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth/customers")
public class CustomerApiController {

    @Autowired private CustomerService service;
    @Autowired private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<?> getListAll() {
        List<Customer> customers = service.getListAll();
        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("List Customer is empty!");
        }
        List<CustomerDTO> customerDTOS = listEntityToDTOS(customers);
        return ResponseEntity.ok(customerDTOS);
    }

    private List<CustomerDTO> listEntityToDTOS(List<Customer> customers) {
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        customers.forEach(customer -> {
            customerDTOS.add(entityToDTO(customer));
        });
        return customerDTOS;
    }

    private CustomerDTO entityToDTO(Customer customer) {
        return mapper.map(customer,CustomerDTO.class);
    }
}
