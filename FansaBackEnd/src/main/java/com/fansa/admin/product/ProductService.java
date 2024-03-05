package com.fansa.admin.product;

import com.fansa.admin.product.request.ProductDTO;
import com.fansa.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    public static final int PRODUCT_PER_PAGE = 12;
    @Autowired private ProductRepository repo;


    public Page<Product> listByPage(Integer pageNum,String sortDir,String sortField,String keyword) {

        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        if (sortField == null || sortField.isEmpty()) {
            sortField = "name";
        }


        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,PRODUCT_PER_PAGE,sort);
        Specification<Product> specification = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            specification = specification.and(ProductSpecifications.withSearchWithName(keyword));
        }

        return repo.findAll(specification,pageable);
    }

    public Product getProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> productInDB = repo.findById(productId);

        if (!productInDB.isPresent()) {
            throw new ProductNotFoundException("product not found with id: " + productId);
        }
        return productInDB.get();
    }

    public void delete(Long productId) throws ProductNotFoundException {
        Optional<Product> productInDB = repo.findById(productId);
        if (!productInDB.isPresent()) {
            throw new ProductNotFoundException("product not found with id: " + productId);
        }
        repo.deleteById(productId);
    }

    public Product create(ProductDTO productDTO) throws ProductNotFoundException {
        Product productInDB = repo.findByName(productDTO.getName());
        if (productInDB != null) {
            throw new ProductNotFoundException("product duplicated with name: " + productInDB.getName());
        }

        productInDB = Product.builder().name(productDTO.getName())
                .alias(unAccent(productDTO.getName()))
                .shortDescription(productDTO.getShortDescription())
                .fullDescription(productDTO.getFullDescription())
                .createdTime(productDTO.getCreatedTime())
                .updatedTime(LocalDate.now())
                .enabled(productDTO.isEnabled())
                .cost(productDTO.getCost())
                .price(productDTO.getPrice())
                .sale(productDTO.getSale())
                .mainImage(productDTO.getMainImage())
                .details(productDTO.getDetails()).build();

        return repo.save(productInDB);
    }

    public Product update(ProductDTO productDTO, Long productId) throws ProductNotFoundException {

        Product productInDB = repo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("product not found with id: " + productId));

        productInDB = Product.builder()
                .id(productId)
                .name(productDTO.getName())
                .alias(unAccent(productDTO.getName()))
                .shortDescription(productDTO.getShortDescription())
                .fullDescription(productDTO.getFullDescription())
                .createdTime(productDTO.getCreatedTime())
                .updatedTime(LocalDate.now())
                .enabled(productDTO.isEnabled())
                .cost(productDTO.getCost())
                .price(productDTO.getPrice())
                .sale(productDTO.getSale())
                .mainImage(productDTO.getMainImage())
                .details(productDTO.getDetails()).build();

        return repo.save(productInDB);
    }

    private String unAccent(String s) {
         return Normalizer
                 .normalize(s,Normalizer.Form.NFD)
                 .replaceAll("[^\\p{ASCII}]", "")
                 .replace(" ","-");

    }

    private float parseFloat(String f) {
        return Float.parseFloat(f);
    }


}
