package com.example.backend.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // TODO: Overall, disabling CSRF and CORS should be done cautiously, and it is important to consider the security implications for your specific application
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class);
        http.authorizeHttpRequests()
                .requestMatchers("/product", "/auth/register", "/auth/login", "/auth/verify", "/error").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
