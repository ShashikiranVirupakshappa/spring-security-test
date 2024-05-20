package com.javabro.springsecurity.repository;

import com.javabro.springsecurity.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role, Long> {
}
