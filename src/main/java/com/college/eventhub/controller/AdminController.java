package com.college.eventhub.controller;

import com.college.eventhub.dto.UserDto;
import com.college.eventhub.entity.User;
import com.college.eventhub.service.AdminService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Builder
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/pending-organizers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> PendingOrganizers(@AuthenticationPrincipal User currentAdmin) {
        return ResponseEntity.ok(adminService.getPendingOrganizers(currentAdmin));
    }

    @PostMapping("/approve/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> approve(@PathVariable Long userId){
        return ResponseEntity.ok(adminService.approveOrganizer(userId));
    }

}
