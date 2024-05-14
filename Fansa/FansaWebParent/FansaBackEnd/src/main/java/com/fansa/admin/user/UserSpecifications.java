package com.fansa.admin.user;

import com.fansa.common.entity.User;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public class UserSpecifications {
     public static Specification<User> withSearchWithNameOrEmailOrId(String keyword) {
        return (root, query, builder) -> {
            Predicate sortName =  builder.or(
                    builder.like(root.get("name"), "%" + keyword + "%" ),
                    builder.like(root.get("email"),"%" + keyword + "%" )
            );
            return builder.and(sortName);
        };
    }
}
