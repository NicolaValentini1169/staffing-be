package com.my_virtual_space.staffing.security.services;

import com.my_virtual_space.staffing.security.entities.Role;
import com.my_virtual_space.staffing.security.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByValue(String value) {
        return roleRepository.findByValue(value);
    }

}
