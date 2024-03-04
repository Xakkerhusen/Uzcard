package com.example.Uzcard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCompanyDTO {
    private String name;
    private String address;
    private String contact;
    private String code ;
    private String phoneNumber;
    private String password;
}
