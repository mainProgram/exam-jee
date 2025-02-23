package com.groupeisi.services.dao;

import com.groupeisi.services.entities.EtudiantEntity;
import com.groupeisi.services.entities.ProfesseurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProfesseurRepository extends JpaRepository<ProfesseurEntity, String> {
    Optional<ProfesseurEntity> findByEmail(String email);
    Optional<ProfesseurEntity> findByPhoneNumber(String phoneNumber);

    @Query("SELECT e FROM ProfesseurEntity e WHERE e.archive = false")
    List<ProfesseurEntity> findAllByArchiveIsFalse();
}