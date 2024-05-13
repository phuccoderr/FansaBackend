package com.fansa.controller.product;

import com.fansa.common.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;



public class ProductSpecification
{
    public static Specification<Product> withSearch(String keyword) {
        return (root, query, builder) -> {
            Predicate enabledPredicate = builder.isTrue(root.get("enabled"));
            Predicate searchPredicate = builder.or(
                    builder.like(root.get("name"), "%" + keyword + "%"),
                    builder.like(root.get("shortDescription"), "%" + keyword + "%"),
                    builder.like(root.get("fullDescription"), "%" + keyword + "%")
            );
            return builder.and(enabledPredicate, searchPredicate);
        };
    }

    public static Specification<Product> withCategory(Long categoryId) {
        return (root, query, builder) -> {
            Predicate enabledPredicate = builder.isTrue(root.get("enabled"));
            Predicate categoryPredicate = builder.or(
                    builder.equal(root.get("category").get("id"), categoryId)
            );
            return builder.and(enabledPredicate, categoryPredicate);
        };
    }
}
