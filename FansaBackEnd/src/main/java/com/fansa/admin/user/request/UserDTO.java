package com.fansa.admin.user.request;

import com.fansa.common.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class UserDTO {
    private String email;
    private String name;
    private List<String> role;
}
