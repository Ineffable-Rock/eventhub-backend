package com.college.eventhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "College")
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false)
    private Integer pinCode;

    @Column(nullable = false)
    @NotBlank
    private String name;

    // link to admin user
    @OneToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private User admin;

    // linking to the members
    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<User> memebers;

}
