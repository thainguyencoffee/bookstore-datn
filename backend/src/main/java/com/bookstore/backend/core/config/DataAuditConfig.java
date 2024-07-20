package com.bookstore.backend.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import java.util.Optional;

@EnableJpaAuditing
public class DataAuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(authentication -> {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof Jwt) {
                        return ((Jwt) principal).getClaim(StandardClaimNames.PREFERRED_USERNAME);
                    } else {
                        return "guest";
                    }
                });
    }

}
