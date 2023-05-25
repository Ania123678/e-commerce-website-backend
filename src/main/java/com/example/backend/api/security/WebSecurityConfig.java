package com.example.backend.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// When a class is marked with @Configuration, it is telling Spring Boot that it contains one or more @Bean methods
// that can be used to configure the application context.
//
// someone requests to access rest API, send the request and we process that request, security filter chain will
// go through the things that you define in HTTP security
@Configuration
public class WebSecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    //SecurityFilterChain is an interface that represents a chain of filters responsible for handling security-related tasks in a web application.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // http.csrf().disable(): This disables CSRF (Cross-Site Request Forgery) protection.
        // cors().disable(): This disables Cross-Origin Resource Sharing (CORS) support.
        // TODO: Overall, disabling CSRF and CORS should be done cautiously, and it is important to consider the security implications for your specific application
        http.csrf().disable().cors().disable();

        // This method is used to add a filter (jwtRequestFilter) before a specified filter (BasicAuthenticationFilter.class) in the filter chain.
        // This means that the JWT authentication filter will be executed before the basic authentication filter during the request processing.
        // The purpose of this configuration is to apply JWT authentication logic and extract the JWT token from the
        // incoming request before the basic authentication filter is invoked.
        // The JWT authentication filter can then validate the token, authenticate the user, and set the appropriate
        // authentication details for the subsequent filters in the chain to use.
        http.addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class);

        // http.authorizeRequests(): This method indicates that you are going to configure authorization rules for specific requests.
        // .requestMatchers("/product").permitAll(): This rule allows unrestricted access (permit all) to the "/product" path.
        // This means that any request to the "/product" path will not require authentication or authorization.
        // .anyRequest().authenticated(): This rule specifies that any other request (not matched by the previous rules) must be authenticated.
        // This means that requests to paths other than "/product", "/auth/register", or "/auth/login" will require authentication.
        http.authorizeHttpRequests()
                .requestMatchers("/product", "/auth/register", "/auth/login", "/auth/verify", "/error").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
