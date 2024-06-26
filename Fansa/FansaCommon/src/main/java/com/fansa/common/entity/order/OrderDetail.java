package com.fansa.common.entity.order;

import com.fansa.common.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float product_cost;
    private int quantity;
    private float total;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    public Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;


}
