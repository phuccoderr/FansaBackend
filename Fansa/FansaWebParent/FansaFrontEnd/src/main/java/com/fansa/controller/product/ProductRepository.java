package com.fansa.controller.product;

import com.fansa.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

    public Product findByAlias(String alias);
}
