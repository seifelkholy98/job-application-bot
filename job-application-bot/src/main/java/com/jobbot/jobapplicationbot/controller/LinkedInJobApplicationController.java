package com.jobbot.jobapplicationbot.controller;

import com.jobbot.jobapplicationbot.model.jobSeeker;
import com.jobbot.jobapplicationbot.service.JobSeekerService;
import com.jobbot.jobapplicationbot.service.LinkedInJobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/linkedin")
public class LinkedInJobApplicationController {

    @Autowired
    private LinkedInJobApplicationService linkedInJobApplicationService;

    @Autowired
    private JobSeekerService jobSeekerService;

    // Endpoint to update LinkedIn credentials
    @PostMapping("/credentials/{jobSeekerId}")
    public void updateLinkedInCredentials(@PathVariable Long jobSeekerId, @RequestParam String username, @RequestParam String password) {
        jobSeeker jobSeeker = jobSeekerService.getJobSeekerById(jobSeekerId);
        if (jobSeeker != null && jobSeeker.isEmailConfirmed()) {
            jobSeeker.setLinkedinUsername(username);
            jobSeeker.setLinkedinPassword(password);
            jobSeekerService.saveJobSeeker(jobSeeker);
        } else {
            throw new IllegalArgumentException("Job Seeker not found or email not confirmed");
        }
    }

    // Endpoint to trigger the job application process
    @PostMapping("/apply/{jobSeekerId}")
    public void applyForJobs(@PathVariable Long jobSeekerId) {
        jobSeeker jobSeeker = jobSeekerService.getJobSeekerById(jobSeekerId);
        if (jobSeeker != null && jobSeeker.isEmailConfirmed()) {
            linkedInJobApplicationService.applyForJobsOnLinkedIn(jobSeeker);
        } else {
            throw new IllegalArgumentException("Job Seeker not found or email not confirmed");
        }
    }
}
