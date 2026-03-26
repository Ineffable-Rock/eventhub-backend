package com.college.eventhub.controller;

import com.college.eventhub.dto.EventRequest;
import com.college.eventhub.dto.EventResponse;
import com.college.eventhub.entity.User;
import com.college.eventhub.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/saveEvent")
    public ResponseEntity<EventResponse> createEvent(
            @RequestBody EventRequest request,
            @AuthenticationPrincipal User organizer
    ) {
        EventResponse savedEvent = eventService.createEvent(request, organizer);

        return ResponseEntity.ok(savedEvent);
    }

    @GetMapping("/allEvent")
    public ResponseEntity<Page<EventResponse>> fetchAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<EventResponse> events = eventService.fetchGlobalList(page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/organizerEvent")
    public ResponseEntity<Page<EventResponse>> fetchPersonalList(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User organier
    ){
        Page<EventResponse> events = eventService.fetchPersonalList(page,size,organier);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Integer eventId) {
        return ResponseEntity.ok(eventService.getEventDetails(eventId));
    }
}