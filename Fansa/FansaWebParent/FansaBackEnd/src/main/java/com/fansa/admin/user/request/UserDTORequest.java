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
import org.hibernate.validator.constraints.Range;

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

    @NotBlank(message = "Password cannot be null")
    @Length(min = 6, max = 64,message = "Password must be between 6 and 64")
    private String password;

    private String confirmPassword;

    @NotEmpty(message = "name cannot be empty")
    private String name;

    private Boolean enabled;

    private Set<Role> roles = new HashSet<>();
}
