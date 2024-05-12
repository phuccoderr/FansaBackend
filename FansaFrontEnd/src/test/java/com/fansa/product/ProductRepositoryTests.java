package com.fansa.product;

import com.fansa.common.entity.Product;
import com.fansa.controller.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {

    @Autowired private ProductRepository repository;

    @Test
    public void findByAlias() {
        String alias = "chu-thuat-su-hoi-chien";
        Product byAlias = repository.findByAlias(alias);
        System.out.println(byAlias.getName());
    }
}
