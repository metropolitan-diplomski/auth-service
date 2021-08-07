package com.met.auth.security;

import com.met.auth.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    public UserDetails getLoggedUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return (UserDetails) principal;

        return null;
    }

    public JwtUser getLoggedJwtUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {

            UserDetails userDetails = (UserDetails) principal;
            JwtUser jwtUser = (JwtUser) userDetails;

            return jwtUser;
        }

        return null;
    }

    public User getLoggedDbUser() {
        return getLoggedJwtUser().getDbUser();
    }

}
