package com.fansa.admin;

import com.fansa.admin.user.RoleRepository;
import com.fansa.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value =  false)
public class RoleRepositoryTests {
    @Autowired private RoleRepository repo;

    @Test
    public void testCreateRoleADMIN() {
        Role role = new Role();
        role.setName("ADMIN");

        Role saved = repo.save(role);
    }

    @Test
    public void testCreateRoleSTAFF() {
        Role role = new Role();
        role.setName("STAFF");

        Role saved = repo.save(role);
    }

    @Test
    public void testFindByName() {
        String name = "ADMIN";
        Role admin = repo.findByName("ADMIN");
        System.out.println(admin.toString());
    }
}
