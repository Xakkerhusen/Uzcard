package com.example.Uzcard.dto;

import com.example.Uzcard.enums.ProfileRole;
import com.example.Uzcard.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private String id;
    private String name;
    private String surname;
    private LocalDateTime createdDate;
    private ProfileStatus status;
    private ProfileRole role;private String password;
    private String email;
    private String phoneNumber;
    private String jwt;

}
