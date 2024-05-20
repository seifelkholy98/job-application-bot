package com.jobbot.jobapplicationbot.repository;

import com.jobbot.jobapplicationbot.model.jobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface jobPostingRepository extends JpaRepository<jobPosting, Long> {
}
