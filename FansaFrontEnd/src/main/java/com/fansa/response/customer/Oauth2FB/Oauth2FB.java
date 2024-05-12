package com.fansa.response.customer.Oauth2FB;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Oauth2FB {
    private String id;
    private String email;
    private String name;
    private Picture picture;
}

