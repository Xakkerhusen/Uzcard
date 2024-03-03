package com.example.Uzcard.repository;

import com.example.Uzcard.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity,Integer> {


    @Query("from SmsHistoryEntity where phone = ?1 order by createdDate desc limit 1")
    Optional<SmsHistoryEntity> findByPhone(String phone);

    @Query("SELECT count (s) from SmsHistoryEntity s where s.phone =?1 and s.createdDate between ?2 and ?3")
    int countSendSMS(String phone, LocalDateTime from, LocalDateTime to);



}
