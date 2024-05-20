package com.jobbot.jobapplicationbot.service;

import com.jobbot.jobapplicationbot.model.jobSeeker;
import com.jobbot.jobapplicationbot.repository.jobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private jobSeekerRepository jobSeekerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<jobSeeker> optionalJobSeeker = jobSeekerRepository.findByEmail(email);
        if (!optionalJobSeeker.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        jobSeeker jobSeeker = optionalJobSeeker.get();
        return new User(jobSeeker.getEmail(), jobSeeker.getPassword(), new ArrayList<>());
    }
}
