package com.jobbot.jobapplicationbot.service;

import com.jobbot.jobapplicationbot.model.jobSeeker;
import com.jobbot.jobapplicationbot.repository.jobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerService {

    @Autowired
    private jobSeekerRepository jobSeekerRepository;

    public List<jobSeeker> getAllJobSeekers() {
        return jobSeekerRepository.findAll();
    }

    public jobSeeker getJobSeekerById(Long id) {
        return jobSeekerRepository.findById(id).orElse(null);
    }

    public jobSeeker saveJobSeeker(jobSeeker jobSeeker) {
        return jobSeekerRepository.save(jobSeeker);
    }

    public void deleteJobSeeker(Long id) {
        jobSeekerRepository.deleteById(id);
    }
}
