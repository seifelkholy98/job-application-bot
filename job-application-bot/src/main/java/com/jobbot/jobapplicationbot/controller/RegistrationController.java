package com.jobbot.jobapplicationbot.controller;

import com.jobbot.jobapplicationbot.dto.UserRegistrationDto;
import com.jobbot.jobapplicationbot.dto.UserLoginDto; // Add this import
import com.jobbot.jobapplicationbot.model.ConfirmationToken;
import com.jobbot.jobapplicationbot.model.jobSeeker;
import com.jobbot.jobapplicationbot.repository.jobSeekerRepository;
import com.jobbot.jobapplicationbot.service.RegistrationService;
import com.jobbot.jobapplicationbot.util.JwtTokenUtil;
import com.jobbot.jobapplicationbot.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private jobSeekerRepository jobSeekerRepository;


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserRegistrationDto registrationDto, UriComponentsBuilder uriBuilder) throws MessagingException {
        registrationService.registerUser(registrationDto, uriBuilder);
        authenticate(registrationDto.getEmail(), registrationDto.getPassword());
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(registrationDto.getEmail());
        return jwtTokenUtil.generateToken(userDetails);
    }

    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam("token") String token) {
        registrationService.confirmEmail(token);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginDto loginDto, UriComponentsBuilder uriBuilder) throws MessagingException {
        authenticate(loginDto.getEmail(), loginDto.getPassword());
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginDto.getEmail());
        if(registrationService.loginUser(loginDto,uriBuilder)){
            return jwtTokenUtil.generateToken(userDetails);
        }else{
            return "You need to confirm your email before logging in";
        }

    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
