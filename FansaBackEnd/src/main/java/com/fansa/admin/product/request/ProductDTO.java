package com.fansa.admin.product.request;

import com.fansa.common.entity.ProductDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Date;
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

    @JsonProperty("main_image")
    private String mainImage;

    Set<ProductDetails> details = new HashSet<>();
}
