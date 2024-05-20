package com.jobbot.jobapplicationbot.service;

import com.jobbot.jobapplicationbot.dto.UserLoginDto;
import com.jobbot.jobapplicationbot.dto.UserRegistrationDto;
import com.jobbot.jobapplicationbot.model.ConfirmationToken;
import com.jobbot.jobapplicationbot.model.jobSeeker;
import com.jobbot.jobapplicationbot.repository.ConfirmationTokenRepository;
import com.jobbot.jobapplicationbot.repository.jobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private jobSeekerRepository jobSeekerRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDto registrationDto, UriComponentsBuilder uriBuilder) throws MessagingException {
        jobSeeker jobSeeker = new jobSeeker();
        jobSeeker.setEmail(registrationDto.getEmail());
        jobSeeker.setFirstName(registrationDto.getFirstName());
        jobSeeker.setLastName(registrationDto.getLastName());
        jobSeeker.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        jobSeeker.setEmailConfirmed(false);

        jobSeekerRepository.save(jobSeeker);

        ConfirmationToken confirmationToken = new ConfirmationToken(jobSeeker);
        confirmationTokenRepository.save(confirmationToken);

        emailConfirmation(registrationDto, uriBuilder, confirmationToken);
    }

    public void emailConfirmation(UserRegistrationDto registrationDto, UriComponentsBuilder uriBuilder, ConfirmationToken confirmationToken) throws MessagingException {
        String confirmationUrl = uriBuilder.path("/register/confirm-email")
                .queryParam("token", confirmationToken.getToken())
                .build()
                .toUriString();

        String emailContent = "<p>Dear " + registrationDto.getFirstName() + ",</p>"
                + "<p>Please click the link below to confirm your email:</p>"
                + "<a href=\"" + confirmationUrl + "\">Confirm Email</a>";

        emailService.sendEmail(registrationDto.getEmail(), "Email Confirmation", emailContent);
    }
    public boolean loginUser(UserLoginDto loginDto, UriComponentsBuilder uriBuilder) throws MessagingException{
        Optional<jobSeeker> User = jobSeekerRepository.findByEmail(loginDto.getEmail());
        if(User.get().isEmailConfirmed()){
            return true ;
        }else{
            jobSeeker jobSeeker = User.get();
            ConfirmationToken confirmationToken = new ConfirmationToken(jobSeeker);
            emailConfirmationForLogin(loginDto,uriBuilder,confirmationToken);
            return false ;
        }

    }
    public void emailConfirmationForLogin(UserLoginDto loginDto, UriComponentsBuilder uriBuilder, ConfirmationToken confirmationToken) throws MessagingException {
        String confirmationUrl = uriBuilder.path("/login/confirm-email")
                .queryParam("token", confirmationToken.getToken())
                .build()
                .toUriString();
        Optional<jobSeeker> jobseeker = jobSeekerRepository.findByEmail(loginDto.getEmail());
        String emailContent = "<p>Dear " + jobseeker.get().getFirstName() + ",</p>"
                + "<p>Please click the link below to confirm your email:</p>"
                + "<a href=\"" + confirmationUrl + "\">Confirm Email</a>";

        emailService.sendEmail(loginDto.getEmail(), "Email Confirmation", emailContent);
    }

    public void confirmEmail(String token) {
        Optional<ConfirmationToken> optionalToken = confirmationTokenRepository.findByToken(token);

        if (optionalToken.isPresent()) {
            ConfirmationToken confirmationToken = optionalToken.get();
            if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Token has expired");
            }

            confirmationToken.setConfirmedAt(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);

            jobSeeker jobSeeker = confirmationToken.getJobSeeker();
            jobSeeker.setEmailConfirmed(true);
            jobSeekerRepository.save(jobSeeker);
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
