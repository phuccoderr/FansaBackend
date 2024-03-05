package com.fansa.admin.user.request;

import com.fansa.common.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDTORequest {
    private Long id;

    @NotBlank(message = "Email cannot be null")
    @Length(max=128, message = "Email must be less than 128 characters")
    @Email(message = "invalid Email")
    private String email;

    @NotNull(message = "password cannot be empty")
    @Length(min=6, message = "password must have 6 characters or more")
    private String password;

    @NotEmpty(message = "name cannot be empty")
    private String name;

    private Boolean enabled;

    @JsonProperty("created_time")
    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    private LocalDate createdTime;

    private Set<Role> roles = new HashSet<>();
}
