package com.fdmgroup.ExpenseTracker.security;

import com.fdmgroup.ExpenseTracker.model.User;
import com.fdmgroup.ExpenseTracker.model.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    public void checkUserAccess(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ROLE_ADMIN.name()));

        if (!isAdmin && !user.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ROLE_ADMIN.name()));
    }

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
} 