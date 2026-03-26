package com.college.eventhub.repository;

import com.college.eventhub.entity.Event;
import com.college.eventhub.entity.EventRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration,Integer> {
    boolean existsByEventIdAndStudentId(Integer eventId, Integer studentId);
    Page<EventRegistration> findByStudentId(Integer id, Pageable pageable);
    Page<EventRegistration> findByEventId(Integer id, Pageable pageable);
}
