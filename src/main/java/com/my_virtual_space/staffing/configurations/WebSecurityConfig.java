package com.my_virtual_space.staffing.configurations;

import com.my_virtual_space.staffing.security.JWTAuthenticationEntryPoint;
import com.my_virtual_space.staffing.security.JWTAuthenticationFilter;
import com.my_virtual_space.staffing.security.services.JWTUserDetailsService;
import com.my_virtual_space.staffing.security.services.UserService;
import com.my_virtual_space.staffing.security.utils.JWTUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe di configurazione per la gestione delle autorizzazioni per le chiamate web
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTAuthenticationEntryPoint unauthorizedHandler;
    private final JWTUserDetailsService jwtUserDetailsService;

    public WebSecurityConfig(JWTAuthenticationEntryPoint unauthorizedHandler, JWTUserDetailsService jwtUserDetailsService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(new JWTUserDetailsService(new UserService()), new JWTUtils());
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(
                        "/api/auth/**",
                        "/v2/api-docs",
                        "/configuration/ui",
//                        "/swagger-resources",
                        "/swagger-resources/**",
//                        "/configuration",
                        "/configuration/**",
                        "/swagger-ui.html",
                        "/webjars/**")
                .permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .logout().permitAll();

        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
