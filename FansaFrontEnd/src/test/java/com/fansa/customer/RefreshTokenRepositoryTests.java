package com.fansa.customer;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RefreshTokenRepositoryTests {
    @Autowired private CustomerRepository customerRepo;
    @Autowired private RefreshTokenRepository refreshTokenRepo;

    @Test
    public void createRefreshToken() {
        Long customerId = 1L;
        Customer customer = customerRepo.findById(customerId).get();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCustomer(customer);

        RefreshToken save = refreshTokenRepo.save(refreshToken);
    }

    @Test
    public void testDeleteRefreshToken() {
        Long customerId = 1L;
        refreshTokenRepo.deleteByCustomer(customerId);
    }

}
