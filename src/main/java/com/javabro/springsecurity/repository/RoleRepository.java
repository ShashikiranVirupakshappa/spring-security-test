package com.javabro.springsecurity.repository;

import com.javabro.springsecurity.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Long> {
    public Optional<Role> findByRoleName(String roleName);
}
