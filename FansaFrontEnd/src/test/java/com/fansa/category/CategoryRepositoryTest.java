package com.fansa.category;

import com.fansa.common.entity.Category;
import com.fansa.controller.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {
    @Autowired private CategoryRepository repository;

    @Test
    public void findByAlias() {
        String alias = "tam-ly-ki-nang-song-1";
        Category category = repository.findByAlias(alias);
        System.out.println(category.getName());
    }
}
