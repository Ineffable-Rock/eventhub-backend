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

        // 1. Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }

        // 2. Build the basic User object
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .idCardUrl(request.getIdCardUrl())
                .build();


        // CASE 1: ADMIN (Creates a new College)
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

            savedAdmin.setCollege(savedCollege);
            userRepository.save(savedAdmin);
        }

        // CASE 2: ORGANIZER (Must join an existing College)
        else if (request.getRole() == Role.ORGANIZER) {
            if (request.getPinCode() == null) {
                throw new RuntimeException("Organizers must provide a College Pincode!");
            }

            College college = collegeRepository.findByPinCode(request.getPinCode())
                    .orElseThrow(() -> new RuntimeException("Invalid Pincode: Your college is not registered yet"));

            user.setCollege(college);
            user.setEnabled(false); // Organizers need approval
            userRepository.save(user);
        }

        // CASE 3: STUDENT (Global User - No College Needed)
        else {
            user.setCollege(null); // Explicitly null (Make sure User entity allows nullable college)
            user.setEnabled(true); // Students are auto-enabled
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
                .build();
    }
}