package com.college.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private String message;
    private String role;
    private String name;
    private Integer pinCode;
    private String email;
    private String collegeName;
    private Boolean isAdminApproved;

    private List<UserDto> pendingUsers;
}