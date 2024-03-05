package com.fansa.common.entity;

import com.fansa.common.IdBasedEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,length = 256,nullable = false)
    private String name;

    @Column(unique = true,length = 256,nullable = false)
    private String alias;

    @Column(name = "short_description",length = Integer.MAX_VALUE)
    private String shortDescription;

    @Column(name = "full_description",length = Integer.MAX_VALUE)
    private String fullDescription;

    @Column(name = "created_time")
    private LocalDate createdTime;

    @Column(name = "updated_time")
    private LocalDate  updatedTime;


    private boolean enabled;


    private float cost;


    private float price;
    private float sale;

    @Column(name = "main_image",nullable = false)

    private String mainImage;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<ProductDetails> details = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getName(), product.getName()) && Objects.equals(getAlias(), product.getAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAlias());
    }
}
