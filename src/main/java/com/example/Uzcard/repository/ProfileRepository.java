package com.example.Uzcard.repository;

import com.example.Uzcard.entity.ProfileEntity;
import com.example.Uzcard.enums.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity,String> {
    Optional<ProfileEntity> findByEmail(String username);

    Optional<ProfileEntity> findByEmailAndPassword(String email, String encode);

    @Transactional
    @Modifying
    @Query("Update ProfileEntity  set status =?2 where id = ?1")
    void updateStatus(String id, Status status);

    Optional<ProfileEntity> findByPhoneNumber(String phone);
}
