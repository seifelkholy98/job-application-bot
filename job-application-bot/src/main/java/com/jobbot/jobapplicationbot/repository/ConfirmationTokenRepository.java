package com.jobbot.jobapplicationbot.repository;

import com.jobbot.jobapplicationbot.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    @Query("SELECT c FROM ConfirmationToken c WHERE c.token = :token")
    Optional<ConfirmationToken> findByToken(@Param("token") String token);
}