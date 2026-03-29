package com.college.eventhub.service;

import com.college.eventhub.dto.*;
import com.college.eventhub.entity.College;
import com.college.eventhub.entity.Role;
import com.college.eventhub.entity.User;
import com.college.eventhub.repository.CollegeRepository;
import com.college.eventhub.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.college.eventhub.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CollegeRepository collegeRepository;

    public AuthenticationResponse register(RegisterRequest request) {


        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }


        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .idCardUrl(request.getIdCardUrl())
                .build();

        College userCollege = null;

        // Case 1: ADMIN (creates a new college)
        if (request.getRole() == Role.ADMIN) {
            if (collegeRepository.findByPinCode(request.getPinCode()).isPresent()) {
                throw new RuntimeException("An admin already exists from your college");
            }

            user.setEnabled(false);
            User savedAdmin = userRepository.save(user);

            College newCollege = College.builder()
                    .name(request.getCollegeName())
                    .pinCode(request.getPinCode())
                    .admin(savedAdmin)
                    .build();

            College savedCollege = collegeRepository.save(newCollege);
            userCollege = savedCollege;

            savedAdmin.setCollege(savedCollege);
            userRepository.save(savedAdmin);
        }

        // Case 2: ORGANIZER (must join an existing college)
        else if (request.getRole() == Role.ORGANIZER) {
            if (request.getPinCode() == null) {
                throw new RuntimeException("Organizers must provide a College Pincode!");
            }

            College college = collegeRepository.findByPinCode(request.getPinCode())
                    .orElseThrow(() -> new RuntimeException("Invalid Pincode: Your college is not registered yet"));
            userCollege = college;
            user.setCollege(college);
            user.setEnabled(false);
            userRepository.save(user);
        }

        // Case 3: Student (Global User -no college needed)
        else {
            user.setCollege(null);
            user.setEnabled(true);
            userRepository.save(user);
        }

        String jwtToken = null;
        if (user.isEnabled()) {
            jwtToken = jwtService.generateToken(user);
        }

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message(user.isEnabled() ? "Registration Successful" : "Account created. Wait for Admin approval.")
                .role(user.getRole().name())
                .name(user.getFullName())
                .email(user.getEmail())
                .collegName(userCollege == null ? null : userCollege.getName())
                .pinCode(userCollege == null ? null : userCollege.getPinCode())
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not exists"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password is not correct");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Account is disabled");
        }

        String jwtToken = jwtService.generateToken(user);

        List<UserDto> pendingUserDtos = null;

        // Only fetch pending users if the logged-in user is an ADMIN
        if (user.getRole() == Role.ADMIN) {
            // Added null check for college just in case
            if (user.getCollege() != null) {
                List<User> rawUsers = userRepository.findByCollegeIdAndRoleAndIsEnabled(
                        user.getCollege().getId(),
                        Role.ORGANIZER,
                        false
                );

                pendingUserDtos = rawUsers.stream()
                        .map(u -> UserDto.builder()
                                .id(u.getId())
                                .fullName(u.getFullName())
                                .email(u.getEmail())
                                .role(u.getRole().name())
                                .idCardUrl(u.getIdCardUrl())
                                .collegeName(u.getCollege().getName())
                                .build())
                        .toList();
            }
        }

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("Login Successful")
                .role(user.getRole().name())
                .pendingUsers(pendingUserDtos)
                .name(user.getFullName())
                .email(user.getEmail())
                .collegName(user.getCollege() != null ? user.getCollege().getName() : null)
                .pinCode(user.getCollege() != null ? user.getCollege().getPinCode() : null)
                .build();
    }
}