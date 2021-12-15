package com.my_virtual_space.staffing.security.repositories;

import com.my_virtual_space.staffing.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

}
