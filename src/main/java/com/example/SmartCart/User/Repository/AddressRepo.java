package com.example.SmartCart.User.Repository;

import com.example.SmartCart.User.Entity.Address;
import com.example.SmartCart.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address,Long> {
    List<Address> findByUserId(Long userId);
}
