package com.fansa.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false,unique = true)
    private String alias;
    private String image;
    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private Set<Category> children = new HashSet<>();

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Product> products = new HashSet<>();

    public void addParent(Category category) {
        this.parent = category;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", children=" + children +
                '}';
    }
}
