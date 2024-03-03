package com.example.Uzcard.dto;

import com.example.Uzcard.enums.CompanyRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDTO {
    private String id;
    private String name;
    private String address;
    private String contact;
    private LocalDateTime createdDate;
    private Boolean visible;
    private CompanyRole role;
    private String code ;
    private String phoneNummber;
    private String password;
    private String jwt;
}
