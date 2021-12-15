package com.my_virtual_space.staffing.security.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my_virtual_space.staffing.constants.ErrorsConstants;
import com.my_virtual_space.staffing.security.constants.SecurityConstants;
import com.my_virtual_space.staffing.security.entities.JWTUserDetail;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Base64;

@Component
public class JWTUtils {
    private static final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    private ObjectMapper mapper;

    @PostConstruct
    public void initialize() {
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String generateTokenFromAuthentication(Authentication authentication) throws JsonProcessingException {
        return generateTokenFromJWTUserDetail((JWTUserDetail) authentication.getPrincipal());
    }

    public String generateTokenFromJWTUserDetail(JWTUserDetail jwtUserDetail) throws JsonProcessingException {
        return Jwts
                .builder()
                .setPayload(mapper.writeValueAsString(jwtUserDetail))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.HEADER_STRING);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(SecurityConstants.TOKEN_PREFIX.length());
        } else if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }

        return null;
    }

    public JWTUserDetail parseToken(String authToken) throws UnexpectedException {
        try {
            return mapper.readValue(new String(Base64.getDecoder().decode(authToken.split("\\.")[1]), StandardCharsets.UTF_8),
                    JWTUserDetail.class);
        } catch (Exception e) {
            log.error("Failed jwt parsing. Original header value: {}", authToken, e);
            throw new UnexpectedException(ErrorsConstants.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(authToken);

            JWTUserDetail jwtUserDetail = parseToken(authToken);

            if (isTokenAhead(jwtUserDetail)) {
                log.warn("Ahead JWT Token");
                return false;
            }

            if (isTokenExpired(jwtUserDetail)) {
                log.warn("Expired JWT Token");
                return false;
            }

            return true;
        } catch (SignatureException e) {
            log.warn("Invalid JWT Signature");
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty.");
        } catch (UnexpectedException e) {
            log.warn("Invalid JWT Token - {}", e.getMessage());
        }

        return false;
    }

    private boolean isTokenAhead(JWTUserDetail token) {
        Long tokenIat = token.getIat();

        if (tokenIat == null) {
            log.warn("Missing iat field in token: {}", token);
            return false;
        }

        try {
            Instant iat = Instant.ofEpochMilli(tokenIat);
            return Instant.now().isBefore(iat);
        } catch (NullPointerException | DateTimeException | ArithmeticException e) {
            log.warn("Failed iat field parsing", e);
            return false;
        }
    }

    private boolean isTokenExpired(JWTUserDetail token) {
        Long tokenExp = token.getExp();

        if (tokenExp == null) {
            log.warn("Missing exp field in token: {}", token);
            return true;
        }

        try {
            Instant exp = Instant.ofEpochMilli(tokenExp);
            return Instant.now().isAfter(exp);
        } catch (NullPointerException | DateTimeException | ArithmeticException e) {
            log.warn("Failed exp field parsing", e);
            return true;
        }
    }
}
