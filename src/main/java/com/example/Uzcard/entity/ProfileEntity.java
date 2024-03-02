package com.example.Uzcard.entity;

import com.example.Uzcard.enums.ProfileRole;
import com.example.Uzcard.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String phoneNumber;
    @Column(name = "created_date")
    protected LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "updated_date")
    protected LocalDateTime updatedDate;
    @Column(name = "visible")
    private Boolean visible = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status= Status.ACTIVE;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProfileRole role;
}
