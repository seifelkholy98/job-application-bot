package com.jobbot.jobapplicationbot.repository;

import com.jobbot.jobapplicationbot.model.jobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface jobSeekerRepository extends JpaRepository<jobSeeker, Long> {
    @Query("SELECT j FROM jobSeeker j WHERE j.email = :email")
    Optional<jobSeeker> findByEmail(@Param("email") String email);
}
