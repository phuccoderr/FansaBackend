package com.fansa.admin.user;

import com.fansa.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    public Role findByName(String name);

    public Long countById(Long id);
}
