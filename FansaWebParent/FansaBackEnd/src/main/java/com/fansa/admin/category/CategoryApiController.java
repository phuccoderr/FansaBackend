package com.fansa.admin.category;

import com.fansa.admin.ErrorDTO;
import com.fansa.admin.category.request.CategoryDTO;
import com.fansa.admin.fileServer.CloudinaryService;
import com.fansa.admin.fileServer.FileUpload;
import com.fansa.admin.fileServer.FileUploadImpl;
import com.fansa.common.entity.Category;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth/categories")
@RequiredArgsConstructor
public class CategoryApiController {
    @Autowired private CategoryService service;
    @Autowired private ModelMapper mapper;
    private final CloudinaryService cloudinaryService;
    private final ErrorDTO errorDTO = new ErrorDTO();


    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        List<Category> categories = service.listAllCategory();
        List<CategoryDTO> categoryDTOS = listEntityToDTOS(categories);

        if (categoryDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("list user is empty!");
        }
        return ResponseEntity.ok(categoryDTOS);
    }

//    @GetMapping
//    public ResponseEntity<?> firstByPage() {
//        return listByPage(1,"asc","name",null);
//    }
//
//    @GetMapping("/page/{pageNum}")
//    public ResponseEntity<?> listByPage(@PathVariable("pageNum")Integer pageNum,
//                                     @RequestParam(value = "sortDir",required = false) String sortDir,
//                                     @RequestParam(value = "sortField",required = false) String sortField,
//                                     @RequestParam(value = "keyword",required = false) String keyword) {
//        Page<Category> page = service.listByPage(pageNum,sortDir,sortField,keyword);
//        List<Category> categories = page.getContent();
//
//        if (categories.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        List<CategoryDTO> categoryDTOS = listEntityToDTOS(categories);
//        long startCount = (pageNum - 1) * service.CATE_PER_PAGE + 1;
//        long endCount = startCount + service.CATE_PER_PAGE - 1;
//        PaginationResponse resultResponse = PaginationResponse.builder()
//                .currentPage(pageNum)
//                .totalPages(page.getTotalPages())
//                .totalItems(page.getTotalElements())
//                .startCount(startCount)
//                .endCount(endCount)
//                .sortField(sortField)
//                .sortDir(sortDir)
//                .keyword(keyword)
//                .results(categoryDTOS).build();
//
//        return ResponseEntity.ok(resultResponse);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long cateId) {
        try {
            Category category = service.getCategory(cateId);
            return ResponseEntity.ok(entityToDTO(category));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestPart("data") @Valid CategoryDTO categoryDTO,
                                            @RequestParam(value = "fileImage",required = false) MultipartFile multipartFile) {
        try {

            if (multipartFile == null || multipartFile.isEmpty()) {
                categoryDTO.setImage("");
            } else {
                String image = this.cloudinaryService.upload(multipartFile);
                categoryDTO.setImage(image);
            }
            Category categoryRequest = service.create(categoryDTO);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(categoryRequest.getId()).toUri();
            return ResponseEntity.created(location).build();
        } catch (CategoryNotFoundException e) {

            errorDTO.setTimestamp(new Date());
            errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            errorDTO.setPath("/categories");
            errorDTO.addError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@RequestPart("data") @Valid CategoryDTO categoryDTO,
                                            @PathVariable("id") Long cateId,
                                            @RequestParam(value = "fileImage",required = false) MultipartFile multipartFile) {
        try {
            if (multipartFile == null || multipartFile.isEmpty())   {
                categoryDTO.setImage("");
            } else {
                String image = this.cloudinaryService.upload(multipartFile);
                categoryDTO.setImage(image);

                Category oldCategory = service.findById(cateId);
                if (!oldCategory.getImage().isEmpty()) {
                    this.cloudinaryService.deleteImage(oldCategory.getImage());
                }
            }
            Category categoryRequest = service.update(categoryDTO, cateId);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(categoryRequest.getId()).toUri();
            return ResponseEntity.created(location).build();
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long cateId) {
        try {
            Category oldCategory = service.delete(cateId);
            if (!oldCategory.getImage().isEmpty()) {
                this.cloudinaryService.deleteImage(oldCategory.getImage());
            }
            return ResponseEntity.ok("Category has been delete with id: " + cateId);
        } catch (CategoryNotFoundException e) {
            errorDTO.setTimestamp(new Date());
            errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
            errorDTO.setPath("/categories");
            errorDTO.addError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        }
    }

    private List<CategoryDTO> listEntityToDTOS(List<Category> categories) {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        categories.forEach(category -> {
            categoryDTOS.add(entityToDTO(category));
        });
        return categoryDTOS;
    }

    private CategoryDTO entityToDTO(Category category) {
        CategoryDTO categoryDTO = mapper.map(category,CategoryDTO.class);
        if (category.getParent() != null) {
            categoryDTO.setParentId(category.getParent().getId());
        }

        categoryDTO.setChildren(category.getChildren());
        return categoryDTO;
    }

}
