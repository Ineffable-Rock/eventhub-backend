package com.college.eventhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Not a valid email")
    private String email;

    @NotBlank(message = "Password should not be empty")
    private String password;

}
