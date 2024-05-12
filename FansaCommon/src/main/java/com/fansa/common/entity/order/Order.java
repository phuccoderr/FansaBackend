package com.fansa.common.entity.order;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.Product;
import com.fansa.common.entity.ProductDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<OrderDetail> orderDetails = new HashSet<>();

    public void addDetail(OrderDetail detail) {
        if (orderDetails == null) {
            orderDetails = new HashSet<>();
        }
        this.orderDetails.add(detail);
    }
}
