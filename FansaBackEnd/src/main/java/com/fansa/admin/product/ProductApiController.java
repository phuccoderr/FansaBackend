package com.fansa.admin.product;

import com.fansa.admin.product.request.ProductDTO;
import com.fansa.admin.PaginationResponse;
import com.fansa.common.entity.Product;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth/products")
public class ProductApiController {
    @Autowired private ProductService service;
    @Autowired private ModelMapper mapper;
    
    @GetMapping()
    public ResponseEntity<?> firstByPage() {
        return listByPage(1,"asc","name",null);
    }
    
    @GetMapping("/page/{pageNum}")
    public ResponseEntity<?> listByPage(@PathVariable("pageNum") Integer pageNum,
                                        @RequestParam(value = "sortDir",required = false) String sortDir,
                                        @RequestParam(value = "sortField",required = false) String sortField,
                                        @RequestParam(value = "keyword",required = false) String keyword) {
        Page<Product> page = service.listByPage(pageNum, sortDir, sortField, keyword);
        List<Product> products = page.getContent();

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ProductDTO> productDTOS = listEntityToDTO(products);

        long startCount = (pageNum - 1) * service.PRODUCT_PER_PAGE + 1;
        long endCount = startCount + service.PRODUCT_PER_PAGE - 1;
        PaginationResponse resultResponse = PaginationResponse.builder()
                .currentPage(pageNum)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .startCount(startCount)
                .endCount(endCount)
                .sortField(sortField)
                .sortDir(sortDir)
                .keyword(keyword)
                .results(productDTOS).build();

        return ResponseEntity.ok(resultResponse);

    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
            Product product = service.getProduct(productId);
            ProductDTO productDTO = entityToDTO(product);
            return ResponseEntity.ok(productDTO);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        try {
            Product product = service.create(productDTO);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(product.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO productDTO,
                                           @PathVariable("id") Long productId) {
        try {
            Product product = service.update(productDTO,productId);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(product.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteProductById(@PathVariable("id") Long productId) {
        try {
            service.delete(productId);
            return ResponseEntity.ok("Product has been deleted with id: " + productId);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    public List<ProductDTO> listEntityToDTO(List<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        products.forEach(
                product -> {
                    productDTOS.add(entityToDTO(product));
                }
        );
        return productDTOS;
    }

    public ProductDTO entityToDTO(Product product) {
        return mapper.map(product, ProductDTO.class);
    }
}
