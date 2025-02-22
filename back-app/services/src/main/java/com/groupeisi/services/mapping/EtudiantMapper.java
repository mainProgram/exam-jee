package com.groupeisi.services.mapping;

import com.groupeisi.services.dto.EtudiantDTO;
import com.groupeisi.services.entities.EtudiantEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EtudiantMapper {

    EtudiantEntity fromEtudiant(EtudiantDTO Etudiant);

    EtudiantDTO toEtudiant(EtudiantEntity EtudiantEntity);

    List<EtudiantDTO> toEtudiants(List<EtudiantEntity> EtudiantEntities);
}
