package com.college.eventhub.repository;

import com.college.eventhub.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College,Integer> {
    // This allows you to find the college (and its admin) using the pin code!
    Optional<College> findByPinCode(Integer pinCode);
}
