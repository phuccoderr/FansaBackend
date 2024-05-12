package com.fansa;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.RefreshToken;
import com.fansa.controller.customer.CustomerInvalidException;
import com.fansa.controller.customer.CustomerService;
import com.fansa.request.RegisterRequest;
import com.fansa.response.customer.CustomerResponse;
import com.fansa.response.customer.Info;
import com.fansa.response.customer.Oauth2FB.Oauth2FB;
import com.fansa.response.customer.Oauth2FB.Oauth2GG;
import com.fansa.security.CustomerDetail;
import com.fansa.security.jwt.JwtResponse;
import com.fansa.security.jwt.JwtService;
import com.fansa.security.jwt.RefreshTokenService;
import com.fansa.security.login.LoginRequest;
import com.fansa.security.login.TokenRefreshRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/hello-world")
    public ResponseEntity<?> helloworld() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginCustomer(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

        if (authenticate.isAuthenticated()) {
            CustomerDetail customerDetail = (CustomerDetail) authenticate.getPrincipal();

            String token = jwtService.generateToken(customerDetail.getUsername());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(customerDetail.getId());

            Info customer = Info.builder()
                    .id(customerDetail.getId())
                    .email(customerDetail.getUsername())
                    .name(customerDetail.getName())
                    .photo(customerDetail.getPhoto()).build();

            CustomerResponse response = new CustomerResponse(customer,token,refreshToken.getToken());
            return ResponseEntity.ok(response);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @GetMapping("/signin/google")
    public ResponseEntity<?> loginGoogle(@RequestParam String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        //Config Header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        // GET USER INFO GOOGLE
        ResponseEntity<Oauth2GG> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Oauth2GG.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Oauth2GG oauth2GG = response.getBody();

            Customer customer = customerService.saveOauth2GG(oauth2GG);

            String token = jwtService.generateToken(customer.getEmail());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(customer.getId());

            Info info = Info.builder()
                    .id(customer.getId())
                    .email(customer.getEmail())
                    .name(customer.getName())
                    .photo(customer.getPhoto()).build();

            CustomerResponse resp = new CustomerResponse(info,token,refreshToken.getToken());
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.status(response.getStatusCode()).body("Error accessing user info");
    }

    @GetMapping("/signin/facebook")
    public ResponseEntity loginFacebook(@RequestParam String accessToken) throws JsonProcessingException {
        String userInfoUrl = String.format("https://graph.facebook.com/me?access_token=%s&fields=name,email,picture",accessToken);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(userInfoUrl,String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            Oauth2FB oauth2Info = mapper.readValue(response.getBody(),Oauth2FB.class);

            Customer customer = customerService.saveOauth2FB(oauth2Info);

            String token = jwtService.generateToken(customer.getEmail());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(customer.getId());

            Info info = Info.builder()
                    .id(customer.getId())
                    .email(customer.getEmail())
                    .name(customer.getName())
                    .photo(customer.getPhoto()).build();
            
            CustomerResponse resp = new CustomerResponse(info,token,refreshToken.getToken());
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.status(response.getStatusCode()).body("Error accessing user info");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            Customer register = customerService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("");
        } catch (CustomerInvalidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid TokenRefreshRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();

        RefreshToken byToken = refreshTokenService.findByToken(requestRefreshToken);
        RefreshToken deleteToken = refreshTokenService.verifyExpiration(byToken);

        String token = jwtService.generateToken(deleteToken.getCustomer().getEmail());

        return ResponseEntity.ok(new JwtResponse(token,deleteToken.getToken(),deleteToken.getCustomer().getEmail()));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutCustomer(@RequestBody @Valid TokenRefreshRequest refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken.getRefreshToken());
        Long customerId = token.getCustomer().getId();
        refreshTokenService.deleteByUserId(customerId);
        return ResponseEntity.ok("Log out Successfully!");
    }

    @GetMapping("/auth/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello anh em!");
    }
}
