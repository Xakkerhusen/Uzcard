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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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


    /**
     *Through this method, ADMIN is able to change the data of
     * the existing company.For him, a companyId comes into the method
     */
    public String update(String id, UpdateCompanyDTO dto, AppLanguage language) {
        Optional<CompanyEntity> optional = companyRepository.findById(id);
        if (optional.isEmpty()) {
            String message = service.getMessage("company.not.fount", language);
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


    /**
     *The information of the company available through this method is paginated.
     */
    public PageImpl<CompanyDTO> pagination(Integer size, Integer page) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<CompanyEntity> companyPage = companyRepository.findAll(pageRequest);
        List<CompanyEntity> list = companyPage.getContent();
        int totalPages = companyPage.getTotalPages();
        List<CompanyDTO> dtoList = new ArrayList<>();
        for (CompanyEntity entity : list) {
            dtoList.add(dto(entity));
        }
        return new PageImpl<>(dtoList, pageRequest, totalPages);
    }

    private CompanyDTO dto(CompanyEntity entity) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setContact(entity.getContact());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setPhoneNummber(entity.getPhoneNummber());
        dto.setVisible(entity.getVisible());
        return dto;

    }

    /**
     * It is modified by the method to copmania (Visible=false).
     * Then it becomes unnecessary to delete the data from the repository.
     */
    public boolean delete(String companyId) {
        get(companyId, AppLanguage.UZ);
        return companyRepository.update(companyId)!=0;
    }

    public CompanyEntity get(String id, AppLanguage language) {
        Optional<CompanyEntity> optional = companyRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadException(service.getMessage("company.not.fount", language));
        }
        return optional.get();
    }
}
