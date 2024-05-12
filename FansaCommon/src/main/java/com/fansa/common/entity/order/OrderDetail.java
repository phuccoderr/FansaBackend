package com.fansa.common.entity.order;

import com.fansa.common.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    public Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;
}
