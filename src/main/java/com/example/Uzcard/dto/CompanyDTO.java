package com.example.Uzcard.dto;

import com.example.Uzcard.enums.CompanyRole;
import com.example.Uzcard.utils.RandomUtil;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Setter
@Getter
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

}
