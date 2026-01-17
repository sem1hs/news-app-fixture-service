package com.semihsahinoglu.fixture_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationEntryPoint entryPoint,
                                                   CustomAccessDeniedHandler deniedHandler) {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint))
                .exceptionHandling(exception -> exception.accessDeniedHandler(deniedHandler))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/v1/fixture/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/internal/fixture/**").hasAuthority("SCOPE_INTERNAL"))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/**").permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
