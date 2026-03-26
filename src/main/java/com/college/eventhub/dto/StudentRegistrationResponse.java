package com.college.eventhub.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class StudentRegistrationResponse {
    private String studentName;
    private String studentEmail;
    private String ticketId;
    private LocalDateTime registrationDate;
    private Map<String, Object> registrationDetails;
}