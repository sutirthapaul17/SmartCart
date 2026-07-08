package com.example.SmartCart.User.Repository;

import com.example.SmartCart.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<User,Long> {
}
