package com.fansa.product;

import com.fansa.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RestController
@RequestMapping("/products")
public class ProductApiController {

    @Autowired private ProductService service;

    @GetMapping
    public ResponseEntity<?> getListAll() {
        List<Product> products = service.getListAll();
        if (products.isEmpty()) {
            return ResponseEntity.ok("Product list is empty!");
        }
        return ResponseEntity.ok(products);
    }
}
