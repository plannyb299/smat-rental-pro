package com.smatech.smatrentalpro.backend.security.repo;


import com.smatech.smatrentalpro.backend.security.models.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {


    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);


    @Modifying
    @Query(value = "delete from confirmation_tokens where user_id = :id", nativeQuery = true)
    void deleteByUser(@Param("id") String id);
}
