package com.fansa.admin.product;

import com.fansa.admin.category.request.CategoryDTO;
import com.fansa.admin.fileServer.CloudinaryService;
import com.fansa.admin.product.request.ProductDTO;
import com.fansa.admin.PaginationResponse;
import com.fansa.admin.product.request.ProductResponse;
import com.fansa.common.entity.Product;
import com.fansa.common.entity.ProductDetail;
import com.fansa.common.entity.ProductImage;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth/products")
public class ProductApiController {
    @Autowired private ProductService service;
    @Autowired private ModelMapper mapper;
    private final CloudinaryService cloudinaryService;

    public ProductApiController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

//    @GetMapping()
//    public ResponseEntity<?> firstByPage() {
//        return listByPage(1,"asc","name",null);
//    }
//
//    @GetMapping("/page/{pageNum}")
//    public ResponseEntity<?> listByPage(@PathVariable("pageNum") Integer pageNum,
//                                        @RequestParam(value = "sortDir",required = false) String sortDir,
//                                        @RequestParam(value = "sortField",required = false) String sortField,
//                                        @RequestParam(value = "keyword",required = false) String keyword) {
//        Page<Product> page = service.listByPage(pageNum, sortDir, sortField, keyword);
//        List<Product> products = page.getContent();
//
//        if (products.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        List<ProductDTO> productDTOS = listEntityToDTO(products);
//
//        long startCount = (pageNum - 1) * service.PRODUCT_PER_PAGE + 1;
//        long endCount = startCount + service.PRODUCT_PER_PAGE - 1;
//        PaginationResponse resultResponse = PaginationResponse.builder()
//                .currentPage(pageNum)
//                .totalPages(page.getTotalPages())
//                .totalItems(page.getTotalElements())
//                .startCount(startCount)
//                .endCount(endCount)
//                .sortField(sortField)
//                .sortDir(sortDir)
//                .keyword(keyword)
//                .results(productDTOS).build();
//
//        return ResponseEntity.ok(resultResponse);
//
//    }

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        List<Product> listProducts = service.getAllProducts();
        List<ProductResponse> productDTOResponses = listEntityToDTO(listProducts);
        if (listProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("list products is empty!");
        }
        return ResponseEntity.ok(productDTOResponses);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
            Product product = service.getProduct(productId);
            ProductResponse productDTOResponses = entityToDTO(product);
            return ResponseEntity.ok(productDTOResponses);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestPart("data") @Valid ProductDTO productDTO,
                                           @RequestParam(value = "mainImage")MultipartFile mainMultipartFile,
                                           @RequestParam(value = "extraImage",required = false) MultipartFile[] extraMultipartFile) {
        try {
            service.getProductByName(productDTO.getName()); //check name
            setMainImage(mainMultipartFile,productDTO,null);
            setExtraImage(extraMultipartFile,productDTO,null);
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
    public ResponseEntity<?> updateProduct(@RequestPart("data") @Valid ProductDTO productDTO,
                                           @PathVariable("id") Long productId,
                                           @RequestParam(value = "mainImage", required = false)MultipartFile mainImage,
                                           @RequestParam(value = "extraImage",required = false)MultipartFile[] extraImage) {
        try {
            Product product = service.getProduct(productId);

            deleteMainImage(mainImage,product);
            deleteExtraImage(extraImage,product);
            setMainImage(mainImage,productDTO,product);
            setExtraImage(extraImage,productDTO,product);
            Product productUpdate = service.update(productDTO,product,productId);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(productUpdate.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteProductById(@PathVariable("id") Long productId) {
        try {
            Product productDelete = service.delete(productId);
            deleteAllImagesProduct(productDelete);
            return ResponseEntity.ok("Product has been deleted with id: " + productId);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    private void setMainImage(MultipartFile mainImage, ProductDTO productDTO,Product product) {
        if (mainImage == null || mainImage.isEmpty() && product != null) {
            productDTO.setMainImage(product.getMainImage());
        } else {
            String upload = this.cloudinaryService.upload(mainImage);
            productDTO.setMainImage(upload);
        }
    }

    private void setExtraImage(MultipartFile[] extraImage,ProductDTO productDTO, Product product) {
        ArrayList<String> images = new ArrayList<>();

        if (extraImage == null || extraImage.length == 0 ) {
            if (product != null) {
                product.getProductImages().forEach(image -> {
                    images.add(image.getName());
                });
                productDTO.setImageDTOs(images);
            } else {
                productDTO.setImageDTOs(images); // is empty
            }
        } else {
            for (MultipartFile file : extraImage) {
                String upload = this.cloudinaryService.upload(file);
                images.add(upload);
            }
            productDTO.setImageDTOs(images);
        }

    }

    private void deleteMainImage(MultipartFile mainImage,Product product) {
        if (mainImage == null || mainImage.isEmpty()) return;
        this.cloudinaryService.deleteImage(product.getMainImage());
    }

    private void deleteExtraImage(MultipartFile[] extraImage,Product product) {
        if (extraImage == null || extraImage.length == 0) return;
        Set<ProductImage> productImages = product.getProductImages();
        for (ProductImage image : productImages) {
            this.cloudinaryService.deleteImage(image.getName());
        }
    }

    private void deleteAllImagesProduct(Product product) {
        this.cloudinaryService.deleteImage(product.getMainImage());
        Set<ProductImage> productImages = product.getProductImages();
        for (ProductImage image : productImages) {
            this.cloudinaryService.deleteImage(image.getName());
        }
    }

    private List<ProductResponse> listEntityToDTO(List<Product> products) {
        List<ProductResponse> productDTOResponses = new ArrayList<>();
        products.forEach(
                product -> {
                    productDTOResponses.add(entityToDTO(product));
                }
        );
        return productDTOResponses;
    }

    private ProductResponse entityToDTO(Product product) {
        ProductResponse productDTOResponse = mapper.map(product, ProductResponse.class);
        productDTOResponse.setDetails(product.getProductDetails());
        for (ProductImage image : product.getProductImages()) {
            productDTOResponse.addImage(image.getName());
        }
        return productDTOResponse;
    }
}
