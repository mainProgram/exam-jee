package com.groupeisi.services.mapping;

import com.groupeisi.services.dto.ProfesseurDTO;
import com.groupeisi.services.dto.User;
import com.groupeisi.services.entities.ProfesseurEntity;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProfesseurMapper {

    @Mapping(target = "user", ignore = true)
    ProfesseurDTO toProfesseurDTO(ProfesseurEntity professeur);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "user.firstname")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phoneNumber", source = "user.userName") // Utilisation de userName comme phoneNumber
    @Mapping(target = "archive", constant = "false")
    ProfesseurEntity toEntity(ProfesseurDTO dto);

    default User userRepresentationToUser(UserRepresentation userRepresentation) {
        if (userRepresentation == null) return null;

        return User.builder()
                .userName(userRepresentation.getUsername())
                .emailId(userRepresentation.getEmail())  // Vérifie bien que l'email est pris ici !
                .firstname(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
    }

    default ProfesseurDTO enrichProfesseurWithUser(ProfesseurEntity professeur, UserRepresentation userRep) {
        ProfesseurDTO dto = toProfesseurDTO(professeur);
        if (userRep != null && userRep.getId() != null) {
            dto.setId(userRep.getId()); // Assurez-vous que l'ID est défini
        }
        dto.setUser(userRepresentationToUser(userRep));
        return dto;
    }
}