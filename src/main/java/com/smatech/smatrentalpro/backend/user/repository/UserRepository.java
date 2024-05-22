package com.smatech.smatrentalpro.backend.user.repository;

import com.smatech.smatrentalpro.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

  List<User> findByApproved(int approved);


    User findByResetPasswordToken(String token);
}
