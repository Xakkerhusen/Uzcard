package com.example.Uzcard.service;

import com.example.Uzcard.dto.*;
import com.example.Uzcard.entity.EmailSendHistoryEntity;
import com.example.Uzcard.entity.ProfileEntity;
import com.example.Uzcard.entity.SmsHistoryEntity;
import com.example.Uzcard.enums.AppLanguage;
import com.example.Uzcard.enums.ProfileRole;
import com.example.Uzcard.enums.SmsStatus;
import com.example.Uzcard.enums.Status;
import com.example.Uzcard.exp.AppBadException;
import com.example.Uzcard.repository.EmailSendHistoryRepository;
import com.example.Uzcard.repository.ProfileRepository;
import com.example.Uzcard.repository.SmsHistoryRepository;
import com.example.Uzcard.utils.JWTUtil;
import com.example.Uzcard.utils.MD5Util;
import com.example.Uzcard.utils.RandomUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private ResourceBundleService resourceBundleService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;

    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    @Autowired
    private SmsServerService smsServerService;
    private int count = 0;


    public ProfileDTO auth(AuthDTO profile, AppLanguage language) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPassword(profile.getEmail(),
                MD5Util.encode(profile.getPassword()));

        if (optional.isEmpty()) {
            log.warn("Email or Password is wrong {}", profile.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("email.or.password.is.wrong", language));
        }

        ProfileEntity entity = optional.get();

        if (!entity.getStatus().equals(Status.ACTIVE)) {
            log.warn("Profile not active {}", entity.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("Profile.not.active", language));
        }

        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setRole(entity.getRole());
        dto.setJwt(JWTUtil.encode(entity.getEmail(), entity.getRole()));

        return dto;
    }

    public Boolean registrationByEmail(RegistrationDTO dto, AppLanguage language) {

        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            if (optional.get().getStatus().equals(Status.REGISTRATION)) {
                profileRepository.delete(optional.get());
            } else {
                log.warn("Email.exists{}", dto.getEmail());
                throw new AppBadException(resourceBundleService.getMessage("Email.exists", language));
            }
        }

        LocalDateTime from = LocalDateTime.now().minusMinutes(1);
        LocalDateTime to = LocalDateTime.now();

        if (emailSendHistoryRepository.countSendEmail(dto.getEmail(), from, to) >= 3) {
            log.warn("To many attempt Please try after 1 minute{}", dto.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("To.many.attempt.Please.try.after.1.minute", language));
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhone());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setStatus(Status.REGISTRATION);
        entity.setRole(ProfileRole.ROLE_USER);
        profileRepository.save(entity);

        String jwt = JWTUtil.encodeForEmail(entity.getId());

        String text = """
                <h1 style="text-align: center"> Click here %s</h1>
                <p style="background-color: #029f2e; color: white; padding: 30px">To complete registration please link to the following link</p>
                <a style=" background-color: #1609c5;
                  color: white;
                  padding: 14px 25px;
                  text-align: center;
                  text-decoration: none;
                  display: inline-block;" href="http://localhost:8080/auth/verification/email/%s
                ">Click</a>
                <br>
                """;
        text = String.format(text, entity.getName(), jwt);
        String message = "Complete registration";

        mailSenderService.sendEmail(dto.getEmail(), message, text);

        EmailSendHistoryEntity email = new EmailSendHistoryEntity();
        email.setMessage(message);
        email.setEmail(dto.getEmail());
        emailSendHistoryRepository.save(email);
        return true;
    }

    public String emailVerification(String jwt, AppLanguage language) {

        try {
            JWTDTO jwtDTO = JWTUtil.decode(jwt);

            Optional<ProfileEntity> optional = profileRepository.findById(jwtDTO.getId());
            if (optional.isEmpty()) {
                log.warn("Profile not found {}", jwtDTO.getId());
                throw new AppBadException(resourceBundleService.getMessage("Profile.not.found", language));
            }
            ProfileEntity entity = optional.get();
            if (!entity.getStatus().equals(Status.REGISTRATION)) {
                log.warn("Profile in wrong status {}", entity.getStatus());
                throw new AppBadException(resourceBundleService.getMessage("Profile.in.wrong.status", language));
            }
            profileRepository.updateStatus(entity.getId(), Status.ACTIVE);
        } catch (JwtException e) {
            log.warn("Please tyre again {}", jwt);
            throw new AppBadException(resourceBundleService.getMessage("Please.try.again", language));
        }
        return null;
    }

    public Boolean registrationByPhoneNumber(RegistrationDTO dto, AppLanguage language) {

        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            if (optional.get().getStatus().equals(Status.REGISTRATION)) {
                profileRepository.delete(optional.get());
            } else {
                log.warn("Email exists {}", dto.getEmail());
                throw new AppBadException(resourceBundleService.getMessage("Email.exists", language));
            }
        }

        LocalDateTime from = LocalDateTime.now().minusMinutes(1);
        LocalDateTime to = LocalDateTime.now();


        if (smsHistoryRepository.countSendSMS(dto.getPhone(), from, to) >= 3) {
            log.warn("To many attempt Please try after 1 minute{}", dto.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("To.many.attempt.Please.try.after.1.minute", language));
        }


        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setStatus(Status.REGISTRATION);
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setPhoneNumber(dto.getPhone());
        profileRepository.save(entity);


        String code = RandomUtil.getRandomSmsCode();
        smsServerService.send(dto.getPhone(), "UzCard test verification code: ", code);
        count = 0;
        return true;
    }


    public String smsVerification(SmsSendDTO dto, AppLanguage language) {
        if (count > 3) {
            log.warn("To many attempt Please try after 1 minute{}", dto.getPhone());
            throw new AppBadException(resourceBundleService.getMessage("To.many.attempt.Please.try.after.1.minute", language));
        }

        Optional<SmsHistoryEntity> byPhone = smsHistoryRepository.findByPhone(dto.getPhone());
        if (byPhone.isEmpty()) {
            log.warn("profile not found{}", dto.getPhone());
            throw new AppBadException(resourceBundleService.getMessage("profile.not.found", language));
        }
        LocalDateTime from = byPhone.get().getCreatedDate().minusMinutes(1);
        LocalDateTime to = LocalDateTime.now();

        if (smsHistoryRepository.countSendSMS(byPhone.get().getPhone(), from, to) >= 3) {
            log.warn("To many attempt Please try after 1 minute{}", dto.getPhone());
            throw new AppBadException(resourceBundleService.getMessage("To.many.attempt.Please.try.after.1.minute", language));
        }

        SmsHistoryEntity sms = byPhone.get();
        if (sms.getCode().equals(dto.getCode())) {
            Optional<ProfileEntity> phone = profileRepository.findByPhoneNumber(dto.getPhone());
            if (phone.isEmpty()) {
                log.warn("Profile not found");
                throw new AppBadException(resourceBundleService.getMessage("Profile.not.found", language));
            }
            ProfileEntity profile = phone.get();
            if (profile.getStatus().equals(Status.REGISTRATION)) {
                profile.setStatus(Status.ACTIVE);
                profile.setPhoneNumber(dto.getPhone());
                profileRepository.save(profile);
                sms.setUsedDate(LocalDateTime.now());
                sms.setStatus(SmsStatus.SEND);
                smsHistoryRepository.save(sms);
                count = 0;
                return "Registered";
            }
        }
        count++;
        return "This code did not match";
    }

}
