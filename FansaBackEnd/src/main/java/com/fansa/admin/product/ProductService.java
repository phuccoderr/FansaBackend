package com.fansa.admin.product;

import com.fansa.admin.category.CategoryRepository;
import com.fansa.admin.product.request.ProductDTO;
import com.fansa.common.entity.Category;
import com.fansa.common.entity.Product;
import com.fansa.common.entity.ProductDetail;
import com.fansa.common.entity.ProductImage;
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
    @Autowired private CategoryRepository cateRepo;

//    public Page<Product> listByPage(Integer pageNum,String sortDir,String sortField,String keyword) {
//
//        if (sortDir == null || sortDir.isEmpty()) {
//            sortDir = "asc";
//        }
//
//        if (sortField == null || sortField.isEmpty()) {
//            sortField = "name";
//        }
//
//
//        Sort sort = Sort.by(sortField);
//        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//
//        Pageable pageable = PageRequest.of(pageNum - 1,PRODUCT_PER_PAGE,sort);
//        Specification<Product> specification = Specification.where(null);
//
//        if (keyword != null && !keyword.isEmpty()) {
//            specification = specification.and(ProductSpecifications.withSearchWithName(keyword));
//        }
//
//        return repo.findAll(specification,pageable);
//    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> productInDB = repo.findById(productId);

        if (!productInDB.isPresent()) {
            throw new ProductNotFoundException("product not found with id: " + productId);
        }
        return productInDB.get();
    }

    public void getProductByName(String name) throws ProductNotFoundException {
        Product productInDB = repo.findByName(name);
        if (productInDB != null ) {
            throw new ProductNotFoundException("product duplicated with name: " + productInDB.getName());
        }
    }

    public Product delete(Long productId) throws ProductNotFoundException {
        Optional<Product> productInDB = repo.findById(productId);
        if (!productInDB.isPresent()) {
            throw new ProductNotFoundException("product not found with id: " + productId);
        }
        repo.deleteById(productId);
        return productInDB.get();
    }

    public Product create(ProductDTO productDTO) throws ProductNotFoundException {

        Product productInDB = Product.builder().name(productDTO.getName())
                .alias(unAccent(productDTO.getName()))
                .shortDescription(productDTO.getShortDescription())
                .fullDescription(productDTO.getFullDescription())
                .createdTime(LocalDate.now())
                .updatedTime(LocalDate.now())
                .enabled(productDTO.isEnabled())
                .cost(productDTO.getCost())
                .price(productDTO.getPrice())
                .sale(productDTO.getSale())
                .mainImage(productDTO.getMainImage()).build();

        Long categoryInForm = productDTO.getCategory();
        Optional<Category> categoryInDB = cateRepo.findById(categoryInForm);
        if (categoryInDB.isPresent()) {
            productInDB.setCategory(categoryInDB.get());
        }

        for (ProductDetail detail : productDTO.getDetails()) {
            productInDB.addDetail(detail.getName(), detail.getValue());
        }

        for (String image : productDTO.getImageDTOs()) {
            productInDB.addImage(image);
        }
        return repo.save(productInDB);
    }

    public Product update(ProductDTO productDTO,Product productInDB, Long productId) throws ProductNotFoundException {


        productInDB = Product.builder()
                .id(productId)
                .name(productDTO.getName())
                .alias(unAccent(productDTO.getName()))
                .shortDescription(productDTO.getShortDescription())
                .fullDescription(productDTO.getFullDescription())
                .createdTime(productInDB.getCreatedTime())
                .updatedTime(LocalDate.now())
                .enabled(productDTO.isEnabled())
                .cost(productDTO.getCost())
                .price(productDTO.getPrice())
                .sale(productDTO.getSale()).build();

        if (productDTO.getMainImage().isEmpty()) {
            productInDB.setMainImage(productInDB.getMainImage());
        } else {
            productInDB.setMainImage(productDTO.getMainImage());
        }


        for (ProductDetail detail : productDTO.getDetails()) {
            productInDB.addDetail(detail.getName(), detail.getValue());
        }

        for (String image : productDTO.getImageDTOs()) {
            productInDB.addImage(image);
        }

        Long categoryInForm = productDTO.getCategory();
        Optional<Category> categoryInDB = cateRepo.findById(categoryInForm);
        if (categoryInDB.isPresent()) {
            productInDB.setCategory(categoryInDB.get());
        }

        return repo.save(productInDB);
    }

    private String unAccent(String s) {
         return Normalizer
                 .normalize(s,Normalizer.Form.NFD)
                 .replaceAll("[^\\p{ASCII}]", "")
                 .replace(" ","-");

    }



}
