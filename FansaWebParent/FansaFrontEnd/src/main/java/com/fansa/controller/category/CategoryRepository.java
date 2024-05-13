package com.fansa.controller.category;

import com.fansa.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT c FROM Category c WHERE c.parent.id is null")
    public List<Category> findRootCategories();

    public Category findByAlias (String alias);
}
