package com.fansa.response;

import com.fansa.common.entity.Category;
import com.fansa.common.entity.ProductDetail;
import com.fansa.common.entity.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String alias;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("full_description")
    private String fullDescription;
    private float cost;


    private float price;
    private float sale;
    @JsonProperty("main_image")
    private String mainImage;
    private Category category;
    @JsonProperty("product_details")
    private Set<ProductDetail> productDetails = new HashSet<>();
    @JsonProperty("product_images")
    private Set<ProductImage> productImages = new HashSet<>();
}
