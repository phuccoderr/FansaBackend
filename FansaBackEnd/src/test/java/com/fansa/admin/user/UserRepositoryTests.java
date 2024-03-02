package com.fansa.admin.user;

import com.fansa.admin.user.RoleRepository;
import com.fansa.admin.user.UserRepository;
import com.fansa.common.entity.Role;
import com.fansa.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {
    @Autowired private UserRepository userRepo;
    @Autowired private RoleRepository roleRepo;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Phuc");
        user.setPassword(passwordEncoder.encode("0123456789"));
        user.setEmail("admin@gmail.com");
        user.setEnabled(true);

        Role admin = roleRepo.findByName("ADMIN");
        user.addRole(admin);

        userRepo.save(user);
        System.out.println(user);
    }

    @Test
    public void testFindByEmail() {
        String email = "admin@gmail.com";
        User user = userRepo.findByEmail(email);
        System.out.println(user);
    }

    @Test
    public void testCountById() {
        Long userId = 10L;
        Long count = userRepo.countById(userId);
        System.out.println(count);
    }
}
