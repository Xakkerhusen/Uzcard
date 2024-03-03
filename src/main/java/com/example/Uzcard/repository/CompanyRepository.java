package com.example.Uzcard.repository;

import com.example.Uzcard.entity.CompanyEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyRepository extends CrudRepository<CompanyEntity, String>, PagingAndSortingRepository<CompanyEntity, String> {
    /**
     *It is modified by the method to copmania (Visible=false).
     */
    @Modifying
    @Transactional
    @Query("update CompanyEntity  set visible=false where id=?1")
    Integer update(String companyID);

}
