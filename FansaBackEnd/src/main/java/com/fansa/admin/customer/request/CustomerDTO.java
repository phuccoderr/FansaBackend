package com.fansa.admin.customer.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerDTO {
    private Long id;
    private String email;
    @JsonProperty("created_time")
    private LocalDate createdTime;
    private boolean enabled;
}
