package com.groupeisi.services.services;

import com.groupeisi.services.dao.IProfesseurRepository;
import com.groupeisi.services.dto.ProfesseurDTO;
import com.groupeisi.services.entities.ProfesseurEntity;
import com.groupeisi.services.exception.EntityExistsException;
import com.groupeisi.services.exception.EntityNotFoundException;
import com.groupeisi.services.exception.KeycloakException;
import com.groupeisi.services.mapping.ProfesseurMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesseurService {

    private final KeycloakService keycloakService;
    private final IProfesseurRepository professeurRepository;
    private final ProfesseurMapper professeurMapper;
    private final MessageSource messageSource;

    public ProfesseurDTO addProfesseur(ProfesseurDTO request) {
        validatePhoneNumberUniqueness(request.getPhoneNumber(), null);

        request.getUser().setUserName(request.getPhoneNumber());
        keycloakService.addUser(request.getUser());
        UserRepresentation userRep = getKeycloakUser(request.getPhoneNumber());

        ProfesseurEntity professeur = professeurMapper.toEntity(request);
        professeur.setId(userRep.getId());

        ProfesseurEntity savedProfesseur = professeurRepository.save(professeur);
        return enrichProfesseurWithUser(savedProfesseur, userRep);
    }

    public ProfesseurDTO updateProfesseur(String id, ProfesseurDTO request) {
        ProfesseurEntity existingProfesseur = findProfesseurById(id);
        validatePhoneNumberUniqueness(request.getPhoneNumber(), id);

        request.getUser().setUserName(request.getPhoneNumber());
        try {
            keycloakService.updateUser(id, request.getUser());
        } catch (Exception e) {
            throw new KeycloakException("Erreur lors de la mise à jour de l'utilisateur Keycloak : " + e.getMessage());
        }
        UserRepresentation userRep = getKeycloakUser(request.getPhoneNumber());

        existingProfesseur.setFirstName(request.getUser().getFirstname());
        existingProfesseur.setLastName(request.getUser().getLastName());
        existingProfesseur.setPhoneNumber(request.getPhoneNumber());
        existingProfesseur.setAddress(request.getAddress());
        existingProfesseur.setArchive(request.getArchive());

        ProfesseurEntity savedProfesseur = professeurRepository.save(existingProfesseur);
        return enrichProfesseurWithUser(savedProfesseur, userRep);
    }
    public ProfesseurDTO getProfesseur(String id) {
        ProfesseurEntity professeur = findProfesseurById(id);
        UserRepresentation userRep = getKeycloakUser(professeur.getPhoneNumber());
        return professeurMapper.enrichProfesseurWithUser(professeur, userRep);
    }

    public List<ProfesseurDTO> getAllProfesseur() {
        return professeurRepository.findAllByArchiveIsFalse().stream()
                .map(professeur -> {
                    UserRepresentation userRep = getKeycloakUser(professeur.getPhoneNumber());
                    return professeurMapper.enrichProfesseurWithUser(professeur, userRep);
                })
                .collect(Collectors.toList());
    }

    public void deleteProfesseur(String id) {
        ProfesseurEntity professeur = findProfesseurById(id);
        professeur.setArchive(true);
        professeurRepository.save(professeur);
    }

    public void validatePhoneNumberUniqueness(String phoneNumber, String excludeId) {
        Optional<ProfesseurEntity> existingProfesseur = professeurRepository.findByPhoneNumber(phoneNumber);
        if (existingProfesseur.isPresent() &&
                (excludeId == null || !existingProfesseur.get().getId().equals(excludeId))) {
            throw new EntityExistsException(
                    messageSource.getMessage("professeur.exists",
                            new Object[]{phoneNumber},
                            Locale.getDefault())
            );
        }
    }

    private void validateRequest(ProfesseurDTO request) {
        if (request == null || request.getUser() == null || !StringUtils.hasText(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Les champs obligatoires sont manquants.");
        }
    }

    private ProfesseurEntity findProfesseurById(String id) {
        return professeurRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        messageSource.getMessage("professeur.notfound",
                                new Object[]{id},
                                Locale.getDefault())
                )
        );
    }

    private UserRepresentation getKeycloakUser(String username) {
        List<UserRepresentation> users = keycloakService.getUser(username);
        if (users.isEmpty()) {
            throw new EntityNotFoundException(
                    messageSource.getMessage("user.notfound",
                            new Object[]{username},
                            Locale.getDefault())
            );
        }
        return users.get(0);
    }

    private ProfesseurDTO enrichProfesseurWithUser(ProfesseurEntity professeur, UserRepresentation userRep) {
        return professeurMapper.enrichProfesseurWithUser(professeur, userRep);
    }

}