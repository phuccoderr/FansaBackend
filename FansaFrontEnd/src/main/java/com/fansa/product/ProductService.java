package com.fansa.product;

import com.fansa.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired private ProductRepository repo;

    public List<Product> getListAll() {
        return repo.findAll();
    }
}
