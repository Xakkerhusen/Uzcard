package com.example.Uzcard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailSendHistoryDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private String message;
    private String email;
}
