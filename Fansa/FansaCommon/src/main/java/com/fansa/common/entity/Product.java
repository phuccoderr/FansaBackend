package com.fansa.common.entity;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ProductDetail> productDetails = new HashSet<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ProductImage> productImages = new HashSet<>();


    public void addDetail(String detailName,String detailValue) {
        if (productDetails == null) {
            productDetails = new HashSet<>();
        }
        this.productDetails.add(new ProductDetail(detailName,detailValue,this));
    }

    public void addImage(String extraImage) {
        if (productImages == null) {
            productImages = new HashSet<>();
        }
        this.productImages.add(new ProductImage(extraImage,this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
