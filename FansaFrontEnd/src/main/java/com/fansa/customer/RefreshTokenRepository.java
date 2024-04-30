package com.fansa.customer;

import com.fansa.common.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    public RefreshToken findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.customer.id = :customerId")
    public void deleteByCustomer(Long customerId);
}
