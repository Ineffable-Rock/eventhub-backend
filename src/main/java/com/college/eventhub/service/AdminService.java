package com.college.eventhub.service;

import com.college.eventhub.dto.UserDto;
import com.college.eventhub.entity.Role;
import com.college.eventhub.entity.User;
import com.college.eventhub.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@RequiredArgsConstructor
@Builder
@Service
public class AdminService {
    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getPendingOrganizers(
            User currentAdmin
    ){

        Integer myCollegeId = currentAdmin.getCollege().getId();
        List<User> rawUsers = userRepository.findByCollegeIdAndRoleAndIsEnabled(
                myCollegeId, Role.ORGANIZER, false
        );

        List<UserDto> cleanList = rawUsers.stream()
                .map(u -> UserDto.builder()
                        .id(u.getId())
                        .fullName(u.getFullName())
                        .email(u.getEmail())
                        .role(u.getRole().name())
                        .idCardUrl(u.getIdCardUrl())
                        .collegeName(u.getCollege().getName())
                        .build())
                .toList();

        return cleanList;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public String approveOrganizer(@PathVariable Long userId){
        User organizer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        organizer.setEnabled(true);
        userRepository.save(organizer);
        return "Organizer approved successfully!";
    }
}

