package com.college.eventhub.repository;

import com.college.eventhub.entity.Role;
import com.college.eventhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findByCollegeIdAndRoleAndIsEnabled(Integer collegeId, Role role, boolean isEnabled);
}
