package com.example.SmartCart.User.Security;

import java.util.Date;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(UserDetails userDetails);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);
}
