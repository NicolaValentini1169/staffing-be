package com.my_virtual_space.staffing.security;

import com.my_virtual_space.staffing.security.services.JWTUserDetailsService;
import com.my_virtual_space.staffing.security.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final JWTUserDetailsService jwtUserDetailsService;
    private final JWTUtils jwtUtils;

    public JWTAuthenticationFilter(JWTUserDetailsService jwtUserDetailsService, JWTUtils jwtUtils) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = jwtUtils.getJWTFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(jwtUtils.parseToken(jwt).getUsername());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            log.error("Could not set user authentication in doFilterInternal method in JWTAuthenticationFilter class in security folder", e);
        }

        filterChain.doFilter(request, response);
    }
}
