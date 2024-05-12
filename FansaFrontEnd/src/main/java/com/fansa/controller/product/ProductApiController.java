package com.fansa.controller.product;

import com.fansa.controller.category.CategoryService;
import com.fansa.common.entity.Category;
import com.fansa.common.entity.Product;
import com.fansa.response.PageResponse;
import com.fansa.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductApiController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;


    @GetMapping("")
    public ResponseEntity<?> firstByPage() {
        return listProduct(null,1,null,null);
    }

    @GetMapping("/page/{pageNum}")
    public ResponseEntity<?> listProduct(@RequestParam(required = false) String alias,
                                         @PathVariable int pageNum,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) String sort_field) {
        Category category = categoryService.getCategory(alias);
        Page<Product> pages = productService.listByPage(pageNum,sort_field,keyword,category);
        List<Product> products = pages.getContent();

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        long startCount = (pageNum - 1) * productService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + productService.PRODUCTS_PER_PAGE - 1;

        PageResponse response = PageResponse.builder()
                .start_count(startCount)
                .end_count(endCount)
                .current_page(pageNum)
                .total_pages(pages.getTotalPages())
                .total_items(pages.getTotalElements())
                .entity(listEntityToDTO(products)).build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{alias}")
    public ResponseEntity<?> getProduct(@PathVariable String alias) {
        try {
            Product product = productService.getProduct(alias);
            return ResponseEntity.ok(entityToDTO(product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private List<ProductResponse> listEntityToDTO(List<Product> products) {
        List<ProductResponse> dtos = new ArrayList<>();
        products.forEach( p -> {
            dtos.add(entityToDTO(p));
        });
        return dtos;
    }

    private ProductResponse entityToDTO(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .alias(product.getAlias())
                .shortDescription(product.getShortDescription())
                .fullDescription(product.getFullDescription())
                .cost(product.getCost())
                .price(product.getPrice())
                .sale(product.getSale())
                .productDetails(product.getProductDetails())
                .productImages(product.getProductImages())
                .mainImage(product.getMainImage()).build();
    }
}
