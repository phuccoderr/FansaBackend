package com.fansa.admin.category;

import com.fansa.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;

import java.text.Normalizer;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
    @Autowired private CategoryRepository repo;
    private Specification<Category> spec = Specification.where(null);

//    @Test
//    public void testCreateCategory() {
//        Category category = Category.builder()
//                .name("Tam ly ki nang song1")
//                .alias(unAccent("Tam ly ki nang song1"))
//                .enabled(true)
//                .build();
//
//        repo.save(category);
//        System.out.println(category.toString());
//    }
//
//    @Test
//    public void testCreateParentCategory() {
//        Category parent = repo.findById(4L).get();
//        Category category = Category.builder()
//                .parent(parent)
//                .name("sub tam ly")
//                .alias(unAccent("sub tam ly"))
//                .enabled(true).build();
//
//        repo.save(category);
//    }
//
//    @Test
//    public void testGetParentCategory() {
//        Category parent = repo.findById(1L).get();
//
//        System.out.println(parent);
//    }
//
//    @Test
//    public void testCategoryDefaultRootCategories() {
//        Sort sort = Sort.by("id");
//        sort = sort.ascending();
//        Pageable page = PageRequest.of(0,12,sort);
//        spec = spec.and(CategorySepcifications.defaultRootCategories());
//        List<Category> all = repo.findAll(spec,page).getContent();
//        System.out.println(all);
//    }
//
//    @Test
//    public void testFindByName() {
//        String name = "Tam ly ki nang song";
//        Category cateInDB = repo.findByName(name);
//        System.out.println(cateInDB);
//    }
//
//    private String unAccent(String s) {
//        return Normalizer
//                .normalize(s,Normalizer.Form.NFD)
//                .replaceAll("[^\\p{ASCII}]", "")
//                .replace(" ", "-");
//    }
//
//    @Test
//    public void testGetCategory() {
//        List<Category> rootCategories = repo.findRootCategories();
//        System.out.println(rootCategories);
//    }
}
