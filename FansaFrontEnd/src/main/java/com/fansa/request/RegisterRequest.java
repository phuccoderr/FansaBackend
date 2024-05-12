package com.fansa.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    public String name;
    @Email(message = "Invalid Email")
    public String email;
    @NotEmpty(message = "password cannot be empty")
    @Length(min=6, message = "password must have 6 characters or more")
    public String password;
}
