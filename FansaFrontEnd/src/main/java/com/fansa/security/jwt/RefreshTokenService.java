package com.fansa.security.jwt;

import com.fansa.common.entity.RefreshToken;
import com.fansa.customer.CustomerRepository;
import com.fansa.customer.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final Long refreshTokenDurationMs = 120000L;

    @Autowired private CustomerRepository customerRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long customerId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .customer(customerRepository.findById(customerId).get())
                .expiryDate(Instant.now().plusMillis(1000L * 60 * 30)) // 30 phut
                .token(UUID.randomUUID().toString())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }


    public void deleteByUserId(Long customerId) {
        refreshTokenRepository.deleteByCustomer(customerId);
    }
}
