package com.college.eventhub.dto;

import com.college.eventhub.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String message;
    private String role;
    private String token;
    private List<User> pendingUsers;
}