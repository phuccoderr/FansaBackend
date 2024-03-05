package com.fansa.admin.product;

import com.fansa.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

    @Autowired private ProductRepository repo;

    @Test
    public void testCreateProduct() {
        Product product = Product.builder()
                .name("Chú Thuật Hồi Chiến - Tập 22")
                .alias("Chu-Thuat-Hoi-Chien-Tap-22")
                .shortDescription("nothing")
                .fullDescription("nothing")
                .createdTime(LocalDate.parse("2019-02-03"))
                .enabled(true)
                .cost(20.5f)
                .price(25.2f)
                .sale(15.5f)
                .mainImage("main_image").build();

        Product save = repo.save(product);
        System.out.println(save.toString());
    }

    @Test
    public void testString() {
        Product product = Product.builder()
                .name("Chú Thuật Hồi Chiến - Tập 21")
                .shortDescription("nothing")
                .fullDescription("nothing")
                .createdTime(LocalDate.parse("2019-02-3"))
                .enabled(true)
                .cost(20)
                .price(25.2f)
                .sale(15)
                .mainImage("main_image").build();


        product.setAlias(unAccent(product.getName()));
        System.out.println(product.getAlias());
    }
    private String unAccent(String s) {
        return Normalizer
                .normalize(s,Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replace(" ","-");

    }
}
