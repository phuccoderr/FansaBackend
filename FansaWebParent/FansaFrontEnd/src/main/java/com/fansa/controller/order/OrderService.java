package com.fansa.controller.order;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.ShoppingCart;
import com.fansa.common.entity.order.Order;
import com.fansa.common.entity.order.OrderDetail;
import com.fansa.controller.customer.CustomerNotFoundException;
import com.fansa.controller.customer.CustomerRepository;
import com.fansa.controller.shoppingcart.ShoppingCartRepository;
import com.fansa.request.CheckoutInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private CustomerRepository customerRepo;
    @Autowired private ShoppingCartRepository cartRepo;


    public void checkOut(CheckoutInfo info,Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("customer not found!"));

        List<ShoppingCart> carts = cartRepo.findByCustomer(customer);

        Order order = new Order();

        carts.forEach(item -> {
            float discount = item.getProduct().getPrice() - item.getProduct().getPrice() * (item.getProduct().getSale() / 100);

            OrderDetail detail = OrderDetail.builder()
                    .product(item.getProduct())
                    .quantity(item.getQuantity())
                    .total(discount * item.getQuantity())
                    .product_cost(discount)
                    .order(order).build();

            order.addDetail(detail);
        });

        order.setCustomer(customer);
        order.setAddress(info.getAddress());
        order.setName(info.getName());
        order.setPayment(info.getPayment());
        order.setPhone(info.getPhone());
        order.setOrder_time(LocalDate.now());
        order.setTotal(info.getTotal());

        orderRepo.save(order);

        cartRepo.deleteAllByCustomer(customer.getId());
    }

    public List<Order> getOrder(Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("customer not found!"));

        return orderRepo.findByCustomer(customer.getId());
    }
}
