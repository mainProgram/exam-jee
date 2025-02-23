package com.groupeisi.services.services;

import com.groupeisi.services.dao.IEtudiantRepository;
import com.groupeisi.services.dto.EtudiantDTO;
import com.groupeisi.services.entities.EtudiantEntity;
import com.groupeisi.services.exception.EntityExistsException;
import com.groupeisi.services.exception.EntityNotFoundException;
import com.groupeisi.services.mapping.EtudiantMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtudiantService {

    private final KeycloakService keycloakService;
    private final IEtudiantRepository etudiantRepository;
    private final EtudiantMapper etudiantMapper;
    private final MessageSource messageSource;

    public EtudiantDTO addEtudiant(EtudiantDTO request) {
        validateEmailUniqueness(request.getUser().getEmailId(), null);

        request.getUser().setUserName(request.getUser().getEmailId());
        keycloakService.addUser(request.getUser());
        UserRepresentation userRep = getKeycloakUser(request.getUser().getUserName());

        EtudiantEntity etudiant = etudiantMapper.toEntity(request);
        etudiant.setId(userRep.getId());
        etudiant.setRegistrationNumber(generateRegistrationNumber());

        EtudiantEntity savedEtudiant = etudiantRepository.save(etudiant);
        return enrichEtudiantWithUser(savedEtudiant, userRep);
    }

    public EtudiantDTO updateEtudiant(String id, EtudiantDTO request) {
        EtudiantEntity existingEtudiant = findEtudiantById(id);
        validateEmailUniqueness(request.getUser().getEmailId(), id);

        request.getUser().setUserName(request.getUser().getEmailId());
        keycloakService.updateUser(id, request.getUser());
        UserRepresentation userRep = getKeycloakUser(request.getUser().getUserName());

        existingEtudiant.setEmail(request.getUser().getEmailId());
        existingEtudiant.setAddress(request.getAddress());
        existingEtudiant.setPhoneNumber(request.getPhoneNumber());
        existingEtudiant.setArchive(request.getArchive());

        EtudiantEntity savedEtudiant = etudiantRepository.save(existingEtudiant);
        return enrichEtudiantWithUser(savedEtudiant, userRep);
    }

    public EtudiantDTO getEtudiant(String id) {
        EtudiantEntity etudiant = findEtudiantById(id);
        UserRepresentation userRep = getKeycloakUser(etudiant.getEmail());
        return enrichEtudiantWithUser(etudiant, userRep);
    }

    public List<EtudiantDTO> getAllEtudiant() {
        return etudiantRepository.findAllByArchiveIsFalse().stream()
                .map(etudiant -> {
                    UserRepresentation userRep = getKeycloakUser(etudiant.getEmail());
                    return enrichEtudiantWithUser(etudiant, userRep);
                })
                .collect(Collectors.toList());
    }

    public void deleteEtudiant(String id) {
        EtudiantEntity etudiant = findEtudiantById(id);
        etudiant.setArchive(true);
        etudiantRepository.save(etudiant);
    }

    public void validateEmailUniqueness(String email, String excludeId) {
        Optional<EtudiantEntity> existingEtudiant = etudiantRepository.findByEmail(email);
        if (existingEtudiant.isPresent() &&
                (excludeId == null || !existingEtudiant.get().getId().equals(excludeId))) {
            throw new EntityExistsException(
                    messageSource.getMessage("etudiant.exists",
                            new Object[]{email},
                            Locale.getDefault())
            );
        }
    }

    private EtudiantEntity findEtudiantById(String id) {
        return etudiantRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        messageSource.getMessage("etudiant.notfound",
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

    private String generateRegistrationNumber() {
        return "SN-MAT-" + (etudiantRepository.findAll().size() + 1);
    }

    private EtudiantDTO enrichEtudiantWithUser(EtudiantEntity etudiant, UserRepresentation userRep) {
        EtudiantDTO dto = etudiantMapper.toEtudiantDTO(etudiant);
        dto.setUser(etudiantMapper.userRepresentationToUser(userRep));
        return dto;
    }
}
