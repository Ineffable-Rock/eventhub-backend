package com.college.eventhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Event name is required")
    @Column(nullable = false)
    private String eventName;

    @NotNull(message = "Start Date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End Date is required")
    private LocalDateTime endDate;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotBlank(message = "Contact number is required")
    private String phoneNo;

    @Email(message = "Email is not valid")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "organizer_id",nullable = false)
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "college_id",nullable = false)
    private College college;

    private String bannerUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> formSchema;
}
