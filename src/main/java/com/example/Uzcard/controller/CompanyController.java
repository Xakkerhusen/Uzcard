package com.example.Uzcard.controller;

import com.example.Uzcard.dto.CompanyDTO;
import com.example.Uzcard.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API create company",description = "It is through the API that the company is created.Only ADMIN can apply to this API.")
    public ResponseEntity<?> create(@RequestBody CompanyDTO dto) {
        return ResponseEntity.ok(companyService.create(dto));
    }

}
