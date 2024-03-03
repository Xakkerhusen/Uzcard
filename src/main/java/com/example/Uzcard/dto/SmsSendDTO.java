package com.example.Uzcard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsSendDTO {
    @NotBlank(message = "Phone  must have a value")
    private String phone;
    @NotBlank(message = "Code  must have a value")
    private String code;
}
