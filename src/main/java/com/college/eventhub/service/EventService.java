package com.college.eventhub.service;

import com.college.eventhub.dto.EventRequest;
import com.college.eventhub.dto.EventResponse;
import com.college.eventhub.entity.Event;
import com.college.eventhub.entity.User;
import com.college.eventhub.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse createEvent(EventRequest request, User organizer) {

        Event event = Event.builder()
                .eventName(request.getEventName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .venue(request.getVenue())
                .phoneNo(request.getPhoneNo())
                .email(request.getEmail())
                .bannerUrl(request.getBannerUrl())
                .organizer(organizer)
                .college(organizer.getCollege())
                .formSchema(request.getFormSchema())
                .build();

        Event savedEvent = eventRepository.save(event);

        return EventResponse.builder()
                .id(savedEvent.getId())
                .eventName(savedEvent.getEventName())
                .description(savedEvent.getDescription())
                .startDate(savedEvent.getStartDate())
                .endDate(savedEvent.getEndDate())
                .venue(savedEvent.getVenue())
                .phoneNo(savedEvent.getPhoneNo())
                .email(savedEvent.getEmail())
                .bannerUrl(savedEvent.getBannerUrl())

                .organizerName(savedEvent.getOrganizer().getFullName())
                .collegeName(savedEvent.getCollege().getName())

                .token(null)
                .build();
    }

    public EventResponse getEventDetails(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return EventResponse.builder()
                // 🟢 Map EVERYTHING here because the user clicked on it
                .id(event.getId())
                .eventName(event.getEventName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .venue(event.getVenue())
                .phoneNo(event.getPhoneNo())
                .email(event.getEmail())
                .bannerUrl(event.getBannerUrl())
                .organizerName(event.getOrganizer().getFullName())
                .collegeName(event.getCollege().getName())
                .formSchema(event.getFormSchema()) // 👈 Blueprint included!
                .build();
    }

    public Page<EventResponse> fetchGlobalList(int page, int size){

        Pageable pageable = PageRequest.of(page,size, Sort.by("startDate").descending());
        Page<Event> eventPage = eventRepository.findAll(pageable);

        return eventPage.map(event -> EventResponse.builder()

                .id(event.getId())
                .eventName(event.getEventName())
                .startDate(event.getStartDate())
                .venue(event.getVenue())
                .bannerUrl(event.getBannerUrl())
                .organizerName(event.getOrganizer().getFullName())
                .collegeName(event.getCollege().getName())
                .build());
    }

    public Page<EventResponse> fetchPersonalList(int page, int size, User organizer){
        Pageable pageable = PageRequest.of(page,size,Sort.by("startDate").descending());
        Integer id = organizer.getId();
        Page<Event> eventPage = eventRepository.findByOrganizerId(id,pageable);

        return eventPage.map(event -> EventResponse.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .venue(event.getVenue())
                .phoneNo(event.getPhoneNo())
                .email(event.getEmail())
                .bannerUrl(event.getBannerUrl())
                .organizerName(event.getOrganizer().getFullName())
                .collegeName(event.getCollege().getName())
                .formSchema(event.getFormSchema())
                .build());
    }
}