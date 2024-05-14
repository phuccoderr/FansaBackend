package com.fansa.admin.product.request;

import com.fansa.common.entity.Category;
import com.fansa.common.entity.ProductDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;

    private String alias;

    @JsonProperty("short_description")
    private String shortDescription;

    @JsonProperty("full_description")
    private String fullDescription;

    @JsonProperty("created_time")
    private LocalDate createdTime;

    @JsonProperty("updated_time")
    private LocalDate updatedTime;

    private boolean enabled;

    private float cost;
    private float price;

    private float sale;
    @JsonProperty("main_image")
    private String mainImage;

    @JsonManagedReference
    private Category category;

    private Set<ProductDetail> details = new HashSet<>();
    @JsonProperty("extra_image")
    private ArrayList<String> imageDTOs = new ArrayList<>();

    public void addImage(String image) {
        this.imageDTOs.add(image);
    }
}
