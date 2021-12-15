package com.my_virtual_space.staffing.security.services;

import com.my_virtual_space.staffing.constants.ErrorsConstants;
import com.my_virtual_space.staffing.dtos.request.UserCredentials;
import com.my_virtual_space.staffing.dtos.response.LoginResponse;
import com.my_virtual_space.staffing.security.constants.SecurityConstants;
import com.my_virtual_space.staffing.security.entities.User;
import com.my_virtual_space.staffing.security.utils.AuthenticationUtils;
import com.my_virtual_space.staffing.security.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.rmi.UnexpectedException;

@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final JWTUtils jwtUtils;
    private final RoleService roleService;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            JWTUtils jwtUtils,
            RoleService roleService,
            UserService userService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse signIn(UserCredentials loginRequest) throws BadCredentialsException, UnexpectedException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info(String.format("User %s logged.", loginRequest.getUsername()));

            return new LoginResponse(
                    AuthenticationUtils.getUserId(),
                    AuthenticationUtils.getUserCn(),
                    AuthenticationUtils.getUserAuthority(),
                    jwtUtils.generateTokenFromAuthentication(authentication));

        } catch (InternalAuthenticationServiceException e) {
            throw new BadCredentialsException(ErrorsConstants.WRONG_USERNAME);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(ErrorsConstants.WRONG_PASSWORD);
        } catch (Exception e) {
            throw new UnexpectedException(ErrorsConstants.INTERNAL_SERVER_ERROR);
        }
    }

    public LoginResponse signUp(UserCredentials registrationRequest) throws BadCredentialsException, UnexpectedException {
        if (userService.existsByUsername(registrationRequest.getUsername())) {
            throw new BadCredentialsException(ErrorsConstants.ALREADY_USE_USERNAME);
        }

        if (registrationRequest.getPassword() == null || registrationRequest.getPassword().replaceAll(" ", "").length() <= 0) {
            throw new BadCredentialsException(ErrorsConstants.NOT_VALID_PASSWORD);
        }

        try {

            User user = new User(
                    registrationRequest.getUsername(),
                    bCryptPasswordEncoder.encode(registrationRequest.getPassword()),
                    roleService.findByValue(SecurityConstants.ROLE_USER_VALUE).orElse(null)
            );

            user = userService.save(user);

            log.info(String.format("User %s registered.", user.getUsername()));

        } catch (Exception e) {
            throw new UnexpectedException(ErrorsConstants.DATABASE_CONNECTION_ERROR);
        }

        return signIn(registrationRequest);
    }

    public String checkToken(String jwt) throws SecurityException {
        if (!Boolean.TRUE.equals(jwtUtils.validateToken(jwt))) {
            throw new BadCredentialsException(ErrorsConstants.DO_LOGIN);
        } else return jwt;
    }
}
