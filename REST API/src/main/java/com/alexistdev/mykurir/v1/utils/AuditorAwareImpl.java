package com.alexistdev.mykurir.v1.utils;

import com.alexistdev.mykurir.v1.models.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.of(currentUser.getEmail());
    }
}