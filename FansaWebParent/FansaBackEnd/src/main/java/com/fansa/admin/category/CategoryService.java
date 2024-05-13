package com.fansa.admin.category;

import com.fansa.admin.category.request.CategoryDTO;
import com.fansa.admin.fileServer.CloudinaryService;
import com.fansa.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    public static final int CATE_PER_PAGE = 12;
    @Autowired private CategoryRepository repo;

    public Category getCategory(Long cateId) throws CategoryNotFoundException {
        return repo.findById(cateId).orElseThrow(() -> new CategoryNotFoundException("Not found category with id:" + cateId));
    }

    public List<Category> listAllCategory() {
        return repo.findAll();
    }


    public Page<Category> listByPage(Integer pageNum,String sortDir,String sortField,String keyword) {
        Sort sort = Sort.by("id");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable page = PageRequest.of(pageNum - 1,CATE_PER_PAGE,sort);

        Specification<Category> spec = Specification.where(null);
        spec = spec.and(CategorySepcifications.defaultRootCategories());
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(CategorySepcifications.searchWithName(keyword));
        }
        return repo.findAll(spec,page);
    }

    public Category create(CategoryDTO categoryDTO) throws CategoryNotFoundException {
        Category cateInDB = repo.findByName(categoryDTO.getName());
        Category parentInDB = null;
        if (categoryDTO.getParentId() != null && categoryDTO.getParentId() != 0) {
            parentInDB = repo.findById(categoryDTO.getParentId()).orElseThrow(
                    () -> new CategoryNotFoundException("Parent category not found with id: " + categoryDTO.getParentId())
            );
        }
        if (cateInDB != null) {
            throw new CategoryNotFoundException("category duplicated with name: " + categoryDTO.getName());
        }

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .alias(unAccent(categoryDTO.getName()))
                .image(categoryDTO.getImage())
                .enabled(categoryDTO.isEnabled())
                .build();

        if (parentInDB != null) {
            category.addParent(parentInDB);
        }
        return repo.save(category);
    }

    public Category findById(Long id) throws CategoryNotFoundException {
        return repo.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("category not found with id: " + id)
        );
    }

    public Category update(CategoryDTO categoryDTO,Long cateId) throws CategoryNotFoundException {
        Category cateInDB = repo.findById(cateId).orElseThrow(
                () -> new CategoryNotFoundException("category not found with id: " + cateId)
        );


        Category parentInDB = null;
        if (categoryDTO.getParentId() != null && categoryDTO.getParentId() != 0) {
            parentInDB = repo.findById(categoryDTO.getParentId()).orElseThrow(
                    () -> new CategoryNotFoundException("Parent category not found with id: " + categoryDTO.getParentId())
            );
        }
        String image = categoryDTO.getImage();
        if (image.isEmpty()) {
           image = cateInDB.getImage();
        }
        cateInDB = Category.builder()
                .id(cateId)
                .name(categoryDTO.getName())
                .alias(unAccent(categoryDTO.getName()))
                .image(image)
                .enabled(categoryDTO.isEnabled())
                .build();
        if (parentInDB != null) {
            cateInDB.addParent(parentInDB);
        }

        return repo.save(cateInDB);
    }

    public Category delete(Long cateId) throws CategoryNotFoundException {
        Category cateInDB = repo.findById(cateId).orElseThrow(
                () -> new CategoryNotFoundException("category not found with id: " + cateId)
        );
        if (cateInDB.getChildren().size() > 0) {
            throw new CategoryNotFoundException("category can not delete because have children!");
        }
        repo.deleteById(cateId);
        return cateInDB;
    }

    private String unAccent(String s) {
        return Normalizer
                .normalize(s,Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replace(" ","-");

    }
}
