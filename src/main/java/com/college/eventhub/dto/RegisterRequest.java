package com.college.eventhub.dto;

import com.college.eventhub.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name could not be blank")
    private String fullName;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private Role role;

    private String collegeName;

    private Integer pinCode;

    private String idCardUrl;

    private Map<String, Object> registrationDetails;

}
