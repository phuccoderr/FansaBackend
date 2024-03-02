package com.fansa.admin.user;

import com.fansa.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    public User findByEmail(String email);

    public Long countById(Long userId);

}
