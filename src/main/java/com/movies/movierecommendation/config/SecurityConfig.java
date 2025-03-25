package com.movies.movierecommendation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.movies.movierecommendation.config.filter.ApiKeyFilter;

@Configuration
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    public SecurityConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/ping").permitAll()
                        .requestMatchers("/admin/apikey/**").hasRole("ADMIN")
                        .requestMatchers("/v1/movies/**").hasAuthority("ROLE_API_USER")
                        .anyRequest().authenticated())
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic -> httpBasic.realmName("MovieRecommendationAPI"));
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(adminUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
