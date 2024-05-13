package com.fansa.controller.order;

import com.fansa.common.entity.order.Order;
import com.fansa.controller.customer.CustomerNotFoundException;
import com.fansa.request.CheckoutInfo;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/auth/order")
@RequiredArgsConstructor
public class OrderApiController {

    @Autowired private OrderService service;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> placeOrder(@PathVariable Long customerId,
                                        @RequestBody CheckoutInfo info) {
        try {
            service.checkOut(info, customerId);
            return ResponseEntity.ok("");
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getOrder(@PathVariable Long customerId) {
        try {
            List<Order> orders = service.getOrder(customerId);
            return ResponseEntity.ok(orders);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
