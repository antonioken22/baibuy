package com.bblets.baibuy.services;

import com.bblets.baibuy.security.AuthUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Integer> {

    private static final Logger log = LoggerFactory.getLogger(AuditorAwareImpl.class);

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            log.debug("AuditorAware: No authentication found in SecurityContext.");
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthUserDetails) {
            AuthUserDetails userDetails = (AuthUserDetails) principal;
            Integer userId = userDetails.getId();
            log.trace("AuditorAware: Found user ID {} from AuthUserDetails.", userId);
            return Optional.ofNullable(userId);
        } else if ("anonymousUser".equals(principal.toString())) {
            log.debug("AuditorAware: Principal is anonymousUser.");
            return Optional.empty();
        } else {
            log.warn("AuditorAware: Could not determine auditor. Principal type: {}", principal.getClass().getName());
            return Optional.empty();
        }
    }
}
