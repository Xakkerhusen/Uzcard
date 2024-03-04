package com.example.Uzcard.controller;

import com.example.Uzcard.dto.*;
import com.example.Uzcard.enums.AppLanguage;
import com.example.Uzcard.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Authorization Api list", description = "Api list for Authorization")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Api for login", description = "this api used for authorization")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthDTO auth,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                            AppLanguage appLanguage) {
        log.info("Login {} ", auth.getEmail());
        return ResponseEntity.ok(authService.auth(auth, appLanguage));
    }


    @PostMapping("/loginCompany")
    @Operation(summary = "Api for login company", description = "this api used for authorization for company")
    public ResponseEntity<CompanyDTO> loginCompany(@Valid @RequestBody AuthCompanyDTO auth,
                                                   @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                   AppLanguage appLanguage) {
        log.info("Login {} ", auth.getPhoneNummber());
        return ResponseEntity.ok(authService.authCompany(auth, appLanguage));
    }


    @PostMapping("/registrationByEmail")
    @Operation(summary = "Api for registration", description = "this api used for registration")
    public ResponseEntity<Boolean> registration(@Valid @RequestBody RegistrationDTO dto,
                                                @RequestParam(value = "Accept-Language", defaultValue = "UZ")
                                                AppLanguage language) {
        log.info("registration by email {} ", dto.getEmail());
        return ResponseEntity.ok(authService.registrationByEmail(dto, language));
    }

    @GetMapping("/verification/email/{jwt}")
    @Operation(summary = "Api for verification by email", description = "this api used for authorization")
    public ResponseEntity<String> emailVerification(@PathVariable("jwt") String jwt,
                                                    @RequestParam(value = "Accept-Language", defaultValue = "UZ")
                                                    AppLanguage language) {
        log.info("emailVerification {} ", jwt);
        return ResponseEntity.ok(authService.emailVerification(jwt, language));
    }


    @PostMapping("/registrationByPhoneNumber")
    @Operation(summary = "Api for registration", description = "this api used for registration")
    public ResponseEntity<Boolean> registrationByPhoneNumber(@Valid @RequestBody RegistrationDTO dto,
                                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                             AppLanguage language) {
        log.info("registration by phoneNumber {} ", dto.getEmail());
        return ResponseEntity.ok(authService.registrationByPhoneNumber(dto, language));
    }


    @GetMapping("/verificationByPhoneNumber")
    @Operation(summary = "Api for verification by email", description = "this api used for authorization")
    public ResponseEntity<String> smsVerification(@RequestBody SmsSendDTO dto,
                                                  @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                  AppLanguage language) {
        log.info("emailVerification {} ", dto.getPhone());
        return ResponseEntity.ok(authService.smsVerification(dto, language));
    }


}
