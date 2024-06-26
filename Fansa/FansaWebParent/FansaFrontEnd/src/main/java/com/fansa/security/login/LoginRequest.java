package com.fansa.security.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email cannot be null")
    @Length(max=128, message = "Email must be less than 128 characters")
    @Email(message = "Invalid Email")
    public String email;
    @NotEmpty(message = "password cannot be empty")
    @Length(min=6, message = "password must have 6 characters or more")
    public String password;
}
