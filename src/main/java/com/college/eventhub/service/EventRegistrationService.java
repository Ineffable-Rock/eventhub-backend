package com.college.eventhub.service;

import com.college.eventhub.dto.EventResponse;
import com.college.eventhub.dto.RegisterRequest;
import com.college.eventhub.dto.StudentRegistrationResponse;
import com.college.eventhub.entity.Event;
import com.college.eventhub.entity.EventRegistration;
import com.college.eventhub.entity.User;
import com.college.eventhub.repository.EventRegistrationRepository;
import com.college.eventhub.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventRegistrationService {
    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    public String registerForEvent(Integer eventId, User student, RegisterRequest request){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event doesn't exists"));

        if(registrationRepository.existsByEventIdAndStudentId(eventId, student.getId())){
            throw new RuntimeException("You are already Registered");
        }

        EventRegistration registration = EventRegistration.builder()
                .event(event)
                .student(student)
                .registrationDate(LocalDateTime.now())
                .ticketId(UUID.randomUUID().toString())
                .registrationDetails(request != null ? request.getRegistrationDetails() : null)
                .build();

        registrationRepository.save(registration);

        return "Registration Successful! Your Ticket ID is: " + registration.getTicketId();
    }

    public Page<EventResponse> fetchPersonalRegistration(int page, int size, User user) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("event.startDate").descending());
        Integer id = user.getId();
        Page<EventRegistration> regPage = registrationRepository.findByStudentId(user.getId(), pageable);

        return regPage.map(reg -> EventResponse.builder()
                .id(reg.getEvent().getId())
                .eventName(reg.getEvent().getEventName())
                .description(reg.getEvent().getDescription())
                .startDate(reg.getEvent().getStartDate())
                .endDate(reg.getEvent().getEndDate())
                .venue(reg.getEvent().getVenue())
                .bannerUrl(reg.getEvent().getBannerUrl())
                .organizerName(reg.getEvent().getOrganizer().getFullName())
                .collegeName(reg.getEvent().getCollege().getName())
                .ticketId(reg.getTicketId())

                .build());
    }

    public Page<StudentRegistrationResponse> getEventRegistrations(Integer eventId, User organizer, int page, int size) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new RuntimeException("Unauthorized: You do not have permission to view this event's registrations.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("registrationDate").descending());
        Page<EventRegistration> registrations = registrationRepository.findByEventId(eventId, pageable);

        return registrations.map(reg -> StudentRegistrationResponse.builder()
                .studentName(reg.getStudent().getFullName())
                .studentEmail(reg.getStudent().getEmail())
                .ticketId(reg.getTicketId())
                .registrationDate(reg.getRegistrationDate())
                .registrationDetails(reg.getRegistrationDetails())
                .build());
    }
}
