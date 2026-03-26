
package com.college.eventhub.controller;

import com.college.eventhub.dto.EventResponse;
import com.college.eventhub.dto.RegisterRequest;
import com.college.eventhub.dto.StudentRegistrationResponse;
import com.college.eventhub.entity.User;
import com.college.eventhub.service.EventRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class EventRegistrationController  {

    private final EventRegistrationService registrationService;

    // TO register for an event
    @PostMapping("/{eventId}")
    public ResponseEntity<String> registerUser(
            @PathVariable Integer eventId,
            @AuthenticationPrincipal User student,
            @RequestBody(required = false) RegisterRequest request
    ) {
        String response = registrationService.registerForEvent(eventId, student, request);
        return ResponseEntity.ok(response);
    }

    // User's personal registration list
    @GetMapping("/userRegistrations")
    public ResponseEntity<Page<EventResponse>> fetchPersonalList(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ){
        Page<EventResponse> events = registrationService.fetchPersonalRegistration(page, size, user);
        return ResponseEntity.ok(events);
    }

    // Organizer's Registration list for an event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<Page<StudentRegistrationResponse>> getRegistrationsForEvent(
            @PathVariable Integer eventId, //  Sending EventID
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User organizer // Sending the user from jwt token
    ) {
        return ResponseEntity.ok(
                registrationService.getEventRegistrations(eventId, organizer, page, size)
        );
    }

}