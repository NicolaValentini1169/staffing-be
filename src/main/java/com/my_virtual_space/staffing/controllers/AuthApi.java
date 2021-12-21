package com.my_virtual_space.staffing.controllers;

import com.my_virtual_space.staffing.dtos.request.UserCredentials;
import com.my_virtual_space.staffing.dtos.response.LoginResponse;
import com.my_virtual_space.staffing.security.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.UnexpectedException;

@RestController
@RequestMapping("/api/auth")
public class AuthApi {

    private final AuthenticationService authenticationService;

    public AuthApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("signIn")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody UserCredentials loginRequest)
            throws UnexpectedException {
        return ResponseEntity.ok(authenticationService.signIn(loginRequest));
    }

    @PostMapping("signUp")
    public ResponseEntity<LoginResponse> registrationUser(@RequestBody UserCredentials registrationRequest)
            throws UnexpectedException {
        return ResponseEntity.ok(authenticationService.signUp(registrationRequest));
    }

    @PostMapping("checkToken")
    public ResponseEntity<?> checkToken(@RequestBody String jwt) {
        return ResponseEntity.ok(authenticationService.checkToken(jwt));
    }

}
