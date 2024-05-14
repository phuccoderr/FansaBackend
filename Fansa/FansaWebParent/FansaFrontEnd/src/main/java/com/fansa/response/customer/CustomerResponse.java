package com.fansa.response.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Info info;
    private String access_token;
    private String refresh_token;
}

