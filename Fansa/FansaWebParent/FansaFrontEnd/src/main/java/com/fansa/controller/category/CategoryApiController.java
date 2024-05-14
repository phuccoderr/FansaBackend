package com.fansa.controller.category;

import com.fansa.common.entity.Category;
import com.fansa.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryApiController {

    @Autowired private CategoryService service;

    @GetMapping
    public ResponseEntity<?> getCategories() {
        List<Category> categories = service.getCategories();

        return ResponseEntity.ok(listEntityToDTO(categories));
    }

    private List<CategoryResponse> listEntityToDTO(List<Category> categories) {
        List<CategoryResponse> dtos = new ArrayList<>();
        categories.forEach( c -> {
            dtos.add(entityToDTO(c));
        });
        return dtos;
    }

    private CategoryResponse entityToDTO (Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .alias(category.getAlias()).build();
    }
}
