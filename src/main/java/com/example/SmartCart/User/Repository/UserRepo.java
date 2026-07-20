package com.example.SmartCart.User.Repository;

import com.example.SmartCart.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    List<User> email(String email);

    Optional<User> findByEmail(String email);
}
