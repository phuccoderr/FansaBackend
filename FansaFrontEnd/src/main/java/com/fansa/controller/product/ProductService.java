package com.fansa.controller.product;

import com.fansa.common.entity.Category;
import com.fansa.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 12;
    @Autowired private ProductRepository repo;



    public Page<Product> listByPage(int pageNum, String sortField, String keyword, Category category) {
        Pageable pageable = PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE);
        Specification<Product> spec = Specification.where(null);

        if (category != null) {
            spec = spec.and(ProductSpecification.withCategory(category.getId()));
        }

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(ProductSpecification.withSearch(keyword));
        }
        System.out.println(sortField);
        if (sortField != null) {
            Sort sort;

            switch (sortField) {
                case "max_price": {
                    sort = Sort.by("price");
                    sort = sort.ascending();
                    return repo.findAll(spec, PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE,sort));
                }
                case "min_price": {
                    sort = Sort.by("price");
                    sort = sort.descending();
                    return repo.findAll(spec, PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE,sort));
                }
                default: {
                    sort = Sort.by("name");
                    sort = sort.ascending();
                    return repo.findAll(spec, PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE,sort));
                }
            }
        }
        return repo.findAll(spec,pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = repo.findByAlias(alias);
        if (product == null) {
            throw new ProductNotFoundException("Product Not Found!");
        }
        return product;
    }
}
