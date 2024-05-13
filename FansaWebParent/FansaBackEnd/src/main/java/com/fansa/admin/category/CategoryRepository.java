package com.fansa.admin.category;

import com.fansa.common.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {

    @Query("SELECT c FROM Category c WHERE c.parent.id is null")
    public List<Category> findRootCategories();

    public Category findByName(String name);
}
