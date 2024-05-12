package com.fansa.common.entity.order;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.Product;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float total;
    private LocalDate order_time;
    @Enumerated(EnumType.STRING)
    private Payment payment;
    private String address;
    private String name;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();
}
