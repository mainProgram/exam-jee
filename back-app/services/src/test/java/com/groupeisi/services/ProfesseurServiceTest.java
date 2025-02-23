package com.groupeisi.services;

import com.groupeisi.services.dao.IProfesseurRepository;
import com.groupeisi.services.dto.ProfesseurDTO;
import com.groupeisi.services.dto.User;
import com.groupeisi.services.entities.ProfesseurEntity;
import com.groupeisi.services.exception.EntityExistsException;
import com.groupeisi.services.exception.EntityNotFoundException;
import com.groupeisi.services.mapping.ProfesseurMapper;
import com.groupeisi.services.services.KeycloakService;
import com.groupeisi.services.services.ProfesseurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ProfesseurServiceTest {

    @InjectMocks
    private ProfesseurService professeurService;

    @Mock
    private IProfesseurRepository professeurRepository;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private ProfesseurMapper professeurMapper;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenReturn("Message par défaut");

        // Simuler un utilisateur par défaut pour tous les appels à getUser
        UserRepresentation defaultUserRep = new UserRepresentation();
        defaultUserRep.setId("DEFAULT_ID");
        defaultUserRep.setEmail("default@example.com");
        defaultUserRep.setUsername("default");
        defaultUserRep.setFirstName("Default");
        defaultUserRep.setLastName("User");
        when(keycloakService.getUser(anyString())).thenReturn(Arrays.asList(defaultUserRep));
    }

    @Test
    void testAddProfesseur_Success() {
        // Arrange
        ProfesseurDTO dto = new ProfesseurDTO();
        dto.setPhoneNumber("1234567890");
        dto.setUser(new User("prof@example.com", "1234567890", "password123", "John", "Doe"));

        ProfesseurEntity entity = new ProfesseurEntity();
        entity.setId("PROF001");
        entity.setPhoneNumber("1234567890");

        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("PROF001");
        userRep.setUsername("1234567890");
        userRep.setEmail("prof@example.com");

        when(professeurRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.empty());
        doNothing().when(keycloakService).addUser(any(User.class));
        when(keycloakService.getUser("1234567890")).thenReturn(List.of(userRep)); // Vérification
        when(professeurMapper.toEntity(dto)).thenReturn(entity);
        when(professeurRepository.save(any(ProfesseurEntity.class))).thenReturn(entity);
        when(professeurMapper.enrichProfesseurWithUser(entity, userRep)).thenReturn(dto);

        // Act
        ProfesseurDTO result = professeurService.addProfesseur(dto);

        // Assert
        assertNotNull(result);
        assertEquals("1234567890", result.getPhoneNumber());
        verify(professeurRepository, times(1)).save(any(ProfesseurEntity.class));
    }

    @Test
    void testUpdateProfesseur_Success() {
        // Arrange
        String id = "PROF001";

        ProfesseurDTO dto = new ProfesseurDTO();
        dto.setId(id);
        dto.setPhoneNumber("0987654321");
        dto.setAddress("456 New Prof Street");
        dto.setUser(new User("updated@example.com", "0987654321", "newPassword123", "Jane", "Doe"));

        ProfesseurEntity existingEntity = new ProfesseurEntity();
        existingEntity.setId(id);
        existingEntity.setFirstName("John");
        existingEntity.setLastName("Doe");
        existingEntity.setPhoneNumber("1234567890");
        existingEntity.setAddress("123 Prof Street");

        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(id);
        userRep.setEmail("updated@example.com");
        userRep.setUsername("0987654321");
        userRep.setFirstName("Jane");
        userRep.setLastName("Doe");

        // Mocks
        when(professeurRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(professeurRepository.findByPhoneNumber("0987654321")).thenReturn(Optional.empty());
        doNothing().when(keycloakService).updateUser(eq(id), any(User.class));
        when(keycloakService.getUser("0987654321")).thenReturn(List.of(userRep));
        when(professeurRepository.save(any(ProfesseurEntity.class))).thenReturn(existingEntity);
        when(professeurMapper.enrichProfesseurWithUser(existingEntity, userRep)).thenReturn(dto);

        // Act
        professeurService.updateProfesseur(id, dto);

        // Vérification après update
        UserRepresentation updatedUser = keycloakService.getUser("0987654321").get(0);
        System.out.println("Email Keycloak après update: " + updatedUser.getEmail());

        // Assert
        assertEquals("updated@example.com", updatedUser.getEmail(), "L'email doit être mis à jour");
    }


    @Test
    void testGetProfesseur_Success() {
        // Arrange
        ProfesseurEntity entity = new ProfesseurEntity();
        entity.setId("PROF001");
        entity.setPhoneNumber("1234567890");

        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("PROF001");
        userRep.setEmail("prof@example.com");

        ProfesseurDTO dto = new ProfesseurDTO();
        dto.setId("PROF001");
        dto.setPhoneNumber("1234567890");

        when(professeurRepository.findById("PROF001")).thenReturn(Optional.of(entity));
        when(keycloakService.getUser("1234567890")).thenReturn(List.of(userRep)); // Correction ici
        when(professeurMapper.enrichProfesseurWithUser(entity, userRep)).thenReturn(dto);

        // Act
        ProfesseurDTO result = professeurService.getProfesseur("PROF001");

        // Assert
        assertNotNull(result);
        assertEquals("1234567890", result.getPhoneNumber());
        verify(professeurRepository, times(1)).findById("PROF001");
    }

    @Test
    void testGetAllProfesseur_Success() {
        // Arrange
        ProfesseurEntity entity1 = new ProfesseurEntity();
        entity1.setId("PROF001");
        entity1.setPhoneNumber("1234567890");
        entity1.setFirstName("John");
        entity1.setLastName("Doe");
        entity1.setArchive(false);

        ProfesseurEntity entity2 = new ProfesseurEntity();
        entity2.setId("PROF002");
        entity2.setPhoneNumber("0987654321");
        entity2.setFirstName("Jane");
        entity2.setLastName("Smith");
        entity2.setArchive(false);

        UserRepresentation userRep1 = new UserRepresentation();
        userRep1.setId("PROF001");
        userRep1.setEmail("prof1@example.com");
        userRep1.setUsername("1234567890");

        UserRepresentation userRep2 = new UserRepresentation();
        userRep2.setId("PROF002");
        userRep2.setEmail("prof2@example.com");
        userRep2.setUsername("0987654321");

        ProfesseurDTO dto1 = new ProfesseurDTO();
        dto1.setId("PROF001");
        dto1.setPhoneNumber("1234567890");
        dto1.setUser(new User("prof1@example.com", "1234567890", null, "John", "Doe"));

        ProfesseurDTO dto2 = new ProfesseurDTO();
        dto2.setId("PROF002");
        dto2.setPhoneNumber("0987654321");
        dto2.setUser(new User("prof2@example.com", "0987654321", null, "Jane", "Smith"));

        when(professeurRepository.findAllByArchiveIsFalse()).thenReturn(Arrays.asList(entity1, entity2));
        when(keycloakService.getUser("1234567890")).thenReturn(List.of(userRep1));
        when(keycloakService.getUser("0987654321")).thenReturn(List.of(userRep2));
        when(professeurMapper.toProfesseurDTO(entity1)).thenReturn(dto1);
        when(professeurMapper.toProfesseurDTO(entity2)).thenReturn(dto2);
        when(professeurMapper.userRepresentationToUser(userRep1)).thenReturn(dto1.getUser());
        when(professeurMapper.userRepresentationToUser(userRep2)).thenReturn(dto2.getUser());

        // Act
        List<ProfesseurDTO> result = professeurService.getAllProfesseur();

        // Assert
        assertEquals(2, result.size(), "Il devrait y avoir 2 professeurs");
        verify(professeurRepository, times(1)).findAllByArchiveIsFalse();
        verify(keycloakService, times(1)).getUser("1234567890");
        verify(keycloakService, times(1)).getUser("0987654321");
    }

    @Test
    void testDeleteProfesseur_Success() {
        // Arrange
        ProfesseurEntity entity = new ProfesseurEntity();
        entity.setId("PROF001");
        entity.setArchive(false);

        ProfesseurEntity archivedEntity = new ProfesseurEntity();
        archivedEntity.setId("PROF001");
        archivedEntity.setArchive(true);

        when(professeurRepository.findById("PROF001")).thenReturn(Optional.of(entity));
        when(professeurRepository.save(any(ProfesseurEntity.class))).thenReturn(archivedEntity);

        // Act
        professeurService.deleteProfesseur("PROF001");

        // Assert
        verify(professeurRepository, times(1)).findById("PROF001");
        verify(professeurRepository, times(1)).save(any(ProfesseurEntity.class));
        assertTrue(archivedEntity.getArchive(), "Le professeur devrait être archivé");
    }

    @Test
    void testValidatePhoneNumberUniqueness_PhoneNumberExists() {
        // Arrange
        String phoneNumber = "1234567890";
        ProfesseurEntity existingProfesseur = new ProfesseurEntity();
        existingProfesseur.setPhoneNumber(phoneNumber);

        when(professeurRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(existingProfesseur));

        // Act & Assert
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                professeurService.validatePhoneNumberUniqueness(phoneNumber, null));
        assertEquals("Message par défaut", exception.getMessage(), "L'exception devrait contenir un message par défaut");
    }

    @Test
    void testGetProfesseur_NotFound() {
        // Arrange
        String id = "PROF999";
        when(professeurRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                professeurService.getProfesseur(id));
        assertEquals("Message par défaut", exception.getMessage(), "L'exception devrait contenir un message par défaut");
    }
}