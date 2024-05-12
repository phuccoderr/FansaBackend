package com.fansa.controller.category;

import com.fansa.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired private CategoryRepository repo;

    public Category getCategory(String alias) {
        return repo.findByAlias(alias);
    }

    public List<Category> getCategories() {
        return repo.findRootCategories();
    }
}
