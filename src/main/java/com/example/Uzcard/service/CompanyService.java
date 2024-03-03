package com.example.Uzcard.service;

import com.example.Uzcard.dto.CompanyDTO;
import com.example.Uzcard.entity.CompanyEntity;
import com.example.Uzcard.enums.CompanyRole;
import com.example.Uzcard.exp.AppBadException;
import com.example.Uzcard.repository.CompanyRepository;
import com.example.Uzcard.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    /**
     *This method will create a company through which the phone number must be no job.
     * Again the company code should be sent to beriberi on the front side only if the role is banking
     * The role is created randomly if there is no BANK.
     */
    public Object create(CompanyDTO dto) {
        CompanyEntity entity = new CompanyEntity();
        entity.setName(dto.getName());
        entity.setContact(dto.getContact());
        entity.setAddress(dto.getAddress());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        if (dto.getRole().equals(CompanyRole.BANK)) {
            entity.setCode(dto.getCode());
        } else {
            entity.setCode(UUID.randomUUID().toString());
        }
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(dto.getVisible());
        entity.setPhoneNummber(dto.getPhoneNummber());
        companyRepository.save(entity);
        return "create successful";
    }

}
