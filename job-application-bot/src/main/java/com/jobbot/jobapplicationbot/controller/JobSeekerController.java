package com.jobbot.jobapplicationbot.controller;

import com.jobbot.jobapplicationbot.model.jobSeeker;
import com.jobbot.jobapplicationbot.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobseekers")
public class JobSeekerController {

    @Autowired
    private JobSeekerService jobSeekerService;

    @GetMapping
    public List<jobSeeker> getAllJobSeekers() {
        return jobSeekerService.getAllJobSeekers();
    }

    @GetMapping("/{id}")
    public jobSeeker getJobSeekerById(@PathVariable Long id) {
        return jobSeekerService.getJobSeekerById(id);
    }

    @PostMapping
    public jobSeeker createJobSeeker(@RequestBody jobSeeker jobSeeker) {
        return jobSeekerService.saveJobSeeker(jobSeeker);
    }

    @PutMapping("/{id}")
    public jobSeeker updateJobSeeker(@PathVariable Long id, @RequestBody jobSeeker updatedJobSeeker) {
        jobSeeker jobSeeker = jobSeekerService.getJobSeekerById(id);
        if (jobSeeker != null && jobSeeker.isEmailConfirmed()) {
            updatedJobSeeker.setId(id);
            return jobSeekerService.saveJobSeeker(updatedJobSeeker);
        } else {
            throw new IllegalArgumentException("Job Seeker not found or email not confirmed");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteJobSeeker(@PathVariable Long id) {
        jobSeekerService.deleteJobSeeker(id);
    }
}
