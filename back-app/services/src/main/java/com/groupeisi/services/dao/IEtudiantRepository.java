package com.groupeisi.services.dao;

import com.groupeisi.services.entities.EtudiantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IEtudiantRepository extends JpaRepository<EtudiantEntity, Long> {
    Optional<EtudiantEntity> findByEmail(String email);
}
