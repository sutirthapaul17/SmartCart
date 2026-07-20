package com.example.SmartCart.User.Security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record JwtUserPrincipal(
        String userId,
        String username,
        List<GrantedAuthority> authorities
) {
}
