package com.example.Uzcard.repository;

import com.example.Uzcard.entity.CompanyEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends CrudRepository<CompanyEntity,String> {

    @Query("from CompanyEntity where phoneNummber = ?1 and password = ?2 and visible = true")
    Optional<CompanyEntity> findByPhoneNummberAndPassword(String phoneNumber, String password);
}
