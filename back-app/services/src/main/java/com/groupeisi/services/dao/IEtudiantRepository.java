package com.groupeisi.services.dao;

import com.groupeisi.services.entities.EtudiantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IEtudiantRepository extends JpaRepository<EtudiantEntity, String> {
    Optional<EtudiantEntity> findByEmail(String email);

    @Query("SELECT e FROM EtudiantEntity e WHERE e.archive = false")
    List<EtudiantEntity> findAllByArchiveIsFalse();
}
