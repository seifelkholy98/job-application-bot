package com.jobbot.jobapplicationbot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class jobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean emailConfirmed = false;

    @ElementCollection
    @CollectionTable(name = "job_seeker_skills", joinColumns = @JoinColumn(name = "job_seeker_id"))
    @Column(name = "skill")
    private List<String> skills;

    @Column(length = 1000) // Adjust length as needed
    private String summary;

    @Column(length = 2000) // Adjust length as needed
    private String experiences;

    @Column(length = 1000) // Adjust length as needed
    private String education;

    @Column(length = 255) // Adjust length as needed
    private String positionTitle;

    @Column(length = 255) // Adjust length as needed
    private String resumePath;

    @Column(length = 255)
    private String linkedinUsername;

    @Column(length = 255)
    private String linkedinPassword;
}
