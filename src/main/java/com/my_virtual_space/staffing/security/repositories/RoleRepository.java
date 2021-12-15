package com.my_virtual_space.staffing.security.repositories;

import com.my_virtual_space.staffing.security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByValue(String value);

}
