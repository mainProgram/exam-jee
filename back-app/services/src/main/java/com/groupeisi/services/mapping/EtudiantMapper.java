package com.groupeisi.services.mapping;

import com.groupeisi.services.dto.EtudiantDTO;
import com.groupeisi.services.dto.User;
import com.groupeisi.services.entities.EtudiantEntity;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EtudiantMapper {

    @Mapping(target = "user", ignore = true)
    EtudiantDTO toEtudiantDTO(EtudiantEntity etudiant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "user.emailId")
    @Mapping(target = "archive", constant = "false")
    EtudiantEntity toEntity(EtudiantDTO dto);

    default User userRepresentationToUser(UserRepresentation userRepresentation) {
        if (userRepresentation == null) return null;

        return User.builder()
                .userName(userRepresentation.getUsername())
                .emailId(userRepresentation.getEmail())
                .firstname(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
    }
}
