package com.college.eventhub.repository;
import com.college.eventhub.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event,Integer> {

    Page<Event> findByOrganizerId(Integer integer, Pageable pagebale);
}
