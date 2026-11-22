package com.example.SmartCart.User.Repository;

import com.example.SmartCart.User.Entity.Address;
import com.example.SmartCart.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address,Long> {
    List<Address> findByUserId(Long userId);
    Optional<Address> findByIdAndUser_Id(
            Long addressId,
            Long userId
    );
}
