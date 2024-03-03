package com.example.Uzcard.entity;

import com.example.Uzcard.enums.CompanyRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "company")
@Setter
@Getter
public class CompanyEntity {
    //    1. Company
//    id(uuid), name, address,contact,created_date,visible, role(BANK,PAYMENT), code (if bank)
//    username (unique),password

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "company_name")
    private String name;

    @Column(name = "company_address")
    private String address;

    @Column(name = "company_contact")
    private String contact;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "visible")
    private Boolean visible=false;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private CompanyRole role;

    @Column(name = "bank_code")
    private String code;
    @Column(name = "bank_phone_nummber",unique = true)
    private String phoneNummber;

    @Column(name = "bank_password")
    private String password;



}
