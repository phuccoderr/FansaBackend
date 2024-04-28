package com.fansa.admin.product.request;

import com.fansa.common.entity.ProductDetail;
import com.fansa.common.entity.ProductImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ProductDTO {

    private Long id;
    @NotBlank(message = "name invalid")
    @Length(max = 256, message = "name must be less than 256 characters")
    private String name;

    private String alias;

    @JsonProperty("short_description")
    private String shortDescription;

    @JsonProperty("full_description")
    private String fullDescription;

    @JsonProperty("created_time")
    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    private LocalDate createdTime;

    @JsonProperty("updated_time")
    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    private LocalDate updatedTime;

    @NotNull(message = "enabled is null")
    private boolean enabled;

    @NotNull(message = "cost invalid")
    private float cost;

    @NotNull(message = "price invalid")
    private float price;

    private float sale;

    @JsonIgnore
    private String mainImage;

    private Long category;

    private Set<ProductDetail> details = new HashSet<>();
    @JsonIgnore
    private ArrayList<String> imageDTOs = new ArrayList<>();

    public void addImage(String image) {
        this.imageDTOs.add(image);
    }

}
