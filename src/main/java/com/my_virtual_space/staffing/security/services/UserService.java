package com.my_virtual_space.staffing.security.services;

import com.my_virtual_space.staffing.security.entities.User;
import com.my_virtual_space.staffing.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User deleteById(UUID id) {
        return userRepository.findById(id).map(user -> {
            userRepository.deleteById(id);

            return user;
        }).orElseThrow(() -> new EntityNotFoundException("Utente non trovato."));
    }

}
