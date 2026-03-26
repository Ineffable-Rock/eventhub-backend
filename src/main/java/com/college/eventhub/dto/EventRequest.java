package com.college.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    private String eventName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String venue;
    private String phoneNo;
    private String email;
    private String bannerUrl;
    private List<Map<String, Object>> formSchema;
}
