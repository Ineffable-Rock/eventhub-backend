package com.college.eventhub.repository;

import com.college.eventhub.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College,Integer> {

    Optional<College> findByPinCode(Integer pinCode);
}
