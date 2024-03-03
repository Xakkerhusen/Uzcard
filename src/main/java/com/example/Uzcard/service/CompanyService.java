package com.example.Uzcard.service;

import com.example.Uzcard.dto.CompanyDTO;
import com.example.Uzcard.dto.UpdateCompanyDTO;
import com.example.Uzcard.entity.CompanyEntity;
import com.example.Uzcard.enums.AppLanguage;
import com.example.Uzcard.enums.CompanyRole;
import com.example.Uzcard.exp.AppBadException;
import com.example.Uzcard.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    private ResourceBundleService service;

    /**
     * This method will create a company through which the phone number must be no job.
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

    public String update(String id, UpdateCompanyDTO dto) {
        Optional<CompanyEntity> optional = companyRepository.findById(id);
        if (optional.isEmpty()) {
            String message = service.getMessage("company.not.fount", AppLanguage.UZ);
            log.warn(message);
            throw new AppBadException(message);
        }
        CompanyEntity entity = optional.get();
        entity.setAddress(dto.getAddress());
        entity.setName(dto.getName());
        entity.setPassword(dto.getPassword());
        entity.setPhoneNummber(dto.getPhoneNumber());
        entity.setCode(dto.getCode());
        entity.setContact(dto.getContact());
        companyRepository.save(entity);
        return "update successful";

    }

}
