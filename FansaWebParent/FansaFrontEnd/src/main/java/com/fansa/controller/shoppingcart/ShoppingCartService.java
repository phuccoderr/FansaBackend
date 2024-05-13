package com.fansa.controller.shoppingcart;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.Product;
import com.fansa.common.entity.ShoppingCart;
import com.fansa.controller.customer.CustomerNotFoundException;
import com.fansa.controller.customer.CustomerRepository;
import com.fansa.controller.product.ProductNotFoundException;
import com.fansa.controller.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {
    @Autowired private ShoppingCartRepository cartRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private CustomerRepository customerRepo;

    public void addProduct(Long productId, Integer quantity, Long customerId)
            throws ProductNotFoundException, CustomerNotFoundException {

        Product product = productRepo.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("product not found!"));

        Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("customer not found!"));

        Integer updateQuantity = quantity;

        ShoppingCart cart = cartRepo.findByCustomerAndProduct(customer, product);
        if (cart != null) {
            updateQuantity = cart.getQuantity() + quantity;
        } else {
             cart = ShoppingCart.builder()
                    .product(product)
                    .customer(customer).build();
        }
        cart.setQuantity(updateQuantity);
        cartRepo.save(cart);

    }

    public List<ShoppingCart> getCarts(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("customer not found!"));

        return cartRepo.findByCustomer(customer);
    }

    @Transactional
    public void removeCart(Long customerId,Long productId) throws ProductNotFoundException, CustomerNotFoundException {
        Product product = productRepo.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("product not found!"));

        Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("customer not found!"));

        ShoppingCart cart = cartRepo.findByCustomerAndProduct(customer, product);
        cartRepo.delete(cart);
    }
}
