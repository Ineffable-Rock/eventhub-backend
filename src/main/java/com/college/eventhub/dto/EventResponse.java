package com.college.eventhub.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponse {

    private Integer id;
    private String eventName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venue;
    private String phoneNo;
    private String email;
    private String bannerUrl;

    private String organizerName;
    private String collegeName;

    private String token;

    private String ticketId;
    private List<Map<String, Object>> formSchema;
}
