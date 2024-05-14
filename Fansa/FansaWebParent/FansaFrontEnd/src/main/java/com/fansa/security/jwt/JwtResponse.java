package com.fansa.security.jwt;

import com.fansa.common.entity.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("refresh_token")
    public String refreshToken;
    public String email;
}
