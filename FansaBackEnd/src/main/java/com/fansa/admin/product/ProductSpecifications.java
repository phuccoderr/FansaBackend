package com.fansa.admin.product;

import com.fansa.common.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<Product> withSearchWithName(String keyword) {
        return (root, query, builder) -> {
            Predicate sortName =  builder.or(
                    builder.like(root.get("name"), "%" + keyword + "%" )
            );
            return builder.and(sortName);
        };
    }
}
