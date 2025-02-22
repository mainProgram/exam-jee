package com.groupeisi.services.services;

import com.groupeisi.services.dao.IEtudiantRepository;
import com.groupeisi.services.dto.EtudiantDTO;
import com.groupeisi.services.dto.User;
import com.groupeisi.services.entities.EtudiantEntity;
import com.groupeisi.services.exception.EntityExistsException;
import com.groupeisi.services.mapping.EtudiantMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EtudiantService {

    private final KeycloakService keycloakService;
    private final IEtudiantRepository etudiantRepository;
    private final EtudiantMapper etudiantMapper;
    private final MessageSource messageSource;

    public EtudiantDTO addEtudiant(EtudiantDTO request) {

        String email = request.getUser().getEmailId();
        if (etudiantRepository.findByEmail(email).isPresent()) {
            throw new EntityExistsException(
                    messageSource.getMessage("etudiant.exists",
                            new Object[]{email},
                            Locale.getDefault())
            );
        }

        request.getUser().setUserName(email);
        keycloakService.addUser(request.getUser());
        UserRepresentation user = keycloakService.getUser(request.getUser().getUserName()).get(0);

        EtudiantEntity etudiant = new EtudiantEntity();
        etudiant.setId(user.getId());
        etudiant.setEmail(request.getUser().getEmailId());
        etudiant.setRegistrationNumber("SN-MAT-" + etudiantRepository.findAll().size()+1);
        etudiant.setAddress(request.getAddress());
        etudiant.setPhoneNumber(request.getPhoneNumber());
        etudiant.setArchive(false);

        User userDTO = new User();
        userDTO.setUserName(user.getUsername());
        userDTO.setEmailId(user.getEmail());
        userDTO.setFirstname(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        EtudiantDTO etudiantDTO = etudiantMapper.toEtudiant(etudiantRepository.save(etudiant));
        etudiantDTO.setUser(userDTO);
        return etudiantDTO;
    }

}
