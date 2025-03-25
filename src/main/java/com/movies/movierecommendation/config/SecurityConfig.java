package com.movies.movierecommendation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.movies.movierecommendation.config.filter.ApiKeyFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private ApiKeyFilter apiKeyFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ping", "/h2-console", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(apiKeyFilter, BasicAuthenticationFilter.class)
                .formLogin(form -> form.disable());
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("adminpassword"))
                .roles("ADMIN")
                .build();
        UserDetails regularUser = User.builder()
                .username("user")
                .password(passwordEncoder.encode("userpassword"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(adminUser, regularUser);
    }
}
