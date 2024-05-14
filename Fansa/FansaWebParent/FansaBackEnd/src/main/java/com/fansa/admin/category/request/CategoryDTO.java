package com.fansa.admin.category.request;

import com.fansa.common.entity.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    @NotBlank(message = "name is empty or null")
    @Length(min = 3, message = "name must be than 3 characters")
    private String name;

    private String alias;

    private String image;
    @NotNull(message = "enabled is null")
    private boolean enabled;

    private Long parentId;
    private Set<Category> children;
}
