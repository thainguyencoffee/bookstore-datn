package com.bookstore.backend.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,
                                "/api/books/{isbn}/reviews",
                                "/api/books/{isbn}/reviews/{id}",
                                "/api/books",
                                "/api/books/{isbn}",
                                "/api/vouchers/**").permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/api/books/{isbn}/reviews",
                                "/api/books/{isbn}/reviews/{id}").authenticated()

                        .requestMatchers(HttpMethod.PATCH,
                                "/api/books/{isbn}/reviews/**",
                                "/api/books/{isbn}/reviews/{id}").authenticated()

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/books/{isbn}/reviews/**",
                                "/api/books/{isbn}/reviews/{id}").authenticated()

                        .anyRequest().hasAnyRole("admin", "employee")
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}