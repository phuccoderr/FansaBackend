package com.fansa;

import com.fansa.common.entity.RefreshToken;
import com.fansa.security.CustomerDetail;
import com.fansa.security.jwt.JwtResponse;
import com.fansa.security.jwt.JwtService;
import com.fansa.security.jwt.RefreshTokenService;
import com.fansa.security.login.LoginRequest;
import com.fansa.security.login.TokenRefreshRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

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
            return ResponseEntity.ok(new JwtResponse(token,refreshToken.getToken(),customerDetail.getUsername()));
        } else {
            throw new UsernameNotFoundException("Invalid user request");
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
