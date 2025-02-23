package com.groupeisi.services;

import com.groupeisi.services.dao.IEtudiantRepository;
import com.groupeisi.services.dto.EtudiantDTO;
import com.groupeisi.services.dto.User;
import com.groupeisi.services.entities.EtudiantEntity;
import com.groupeisi.services.exception.EntityExistsException;
import com.groupeisi.services.exception.EntityNotFoundException;
import com.groupeisi.services.mapping.EtudiantMapper;
import com.groupeisi.services.services.EtudiantService;
import com.groupeisi.services.services.KeycloakService;
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

public class EtudiantServiceTest {

    @InjectMocks
    private EtudiantService etudiantService;

    @Mock
    private IEtudiantRepository etudiantRepository;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private EtudiantMapper etudiantMapper;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenReturn("Message par défaut");
    }

    @Test
    void testAddEtudiant_Success() {
        // Arrange
        EtudiantDTO dto = new EtudiantDTO();
        dto.setId("ET001"); // to verify (sha)
        dto.setPhoneNumber("1234567890");
        dto.setAddress("GT Rue 69x54");
        dto.setRegistrationNumber(null);
        dto.setArchive(false);
        dto.setUser(new User("etudiant@example.com", "etudiant@example.com", "password123", "John", "Doe"));

        EtudiantEntity entity = new EtudiantEntity();
        entity.setId("ET001");
        entity.setPhoneNumber("1234567890");
        entity.setAddress("GT Rue 69x54");
        entity.setRegistrationNumber("SN-MAT-1");
        entity.setArchive(false);

        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("ET001");
        userRep.setEmail("etudiant@example.com");
        userRep.setUsername("etudiant@example.com");
        userRep.setFirstName("John");
        userRep.setLastName("Doe");

        when(etudiantRepository.findByEmail("etudiant@example.com")).thenReturn(Optional.empty());
        doNothing().when(keycloakService).addUser(any(User.class));
        when(keycloakService.getUser("etudiant@example.com")).thenReturn(List.of(userRep));
        when(etudiantMapper.toEntity(dto)).thenReturn(entity);
        when(etudiantMapper.toEtudiantDTO(entity)).thenReturn(dto);
        when(etudiantMapper.userRepresentationToUser(userRep)).thenReturn(dto.getUser());
        when(etudiantRepository.save(any(EtudiantEntity.class))).thenReturn(entity);
        when(etudiantRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        EtudiantDTO result = etudiantService.addEtudiant(dto);

        // Assert
        assertEquals("ET001", result.getId(), "L'ID devrait correspondre");
        assertEquals("etudiant@example.com", result.getUser().getEmailId(), "L'email devrait correspondre");
        verify(etudiantRepository, times(1)).save(any(EtudiantEntity.class));
        verify(keycloakService, times(1)).addUser(any(User.class));
        verify(keycloakService, times(1)).getUser("etudiant@example.com");
    }

    @Test
    void testUpdateEtudiant_Success() {
        // Arrange
        EtudiantDTO dto = new EtudiantDTO();
        dto.setId("ET001");
        dto.setPhoneNumber("0987654321");
        dto.setAddress("456 New Street");
        dto.setUser(new User("updated@example.com", "updated@example.com", "newPassword123", "Jane", "Doe"));

        EtudiantEntity existingEntity = new EtudiantEntity();
        existingEntity.setId("ET001");
        existingEntity.setPhoneNumber("1234567890");
        existingEntity.setAddress("GT Rue 69x54");
        existingEntity.setEmail("etudiant@example.com");

        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("ET001");
        userRep.setEmail("updated@example.com");
        userRep.setUsername("updated@example.com");
        userRep.setFirstName("Jane");
        userRep.setLastName("Doe");

        when(etudiantRepository.findById("ET001")).thenReturn(Optional.of(existingEntity));
        when(etudiantRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        doNothing().when(keycloakService).updateUser(eq("ET001"), any(User.class));
        when(keycloakService.getUser("updated@example.com")).thenReturn(List.of(userRep));
        when(etudiantRepository.save(any(EtudiantEntity.class))).thenReturn(existingEntity);
        when(etudiantMapper.toEtudiantDTO(existingEntity)).thenReturn(dto);
        when(etudiantMapper.userRepresentationToUser(userRep)).thenReturn(dto.getUser());

        // Act
        EtudiantDTO result = etudiantService.updateEtudiant("ET001", dto);

        // Assert
        assertEquals("0987654321", result.getPhoneNumber(), "Le numéro de téléphone devrait être mis à jour");
        assertEquals("updated@example.com", result.getUser().getEmailId(), "L'email devrait être mis à jour");
        verify(etudiantRepository, times(1)).findById("ET001");
        verify(etudiantRepository, times(1)).save(any(EtudiantEntity.class));
        verify(keycloakService, times(1)).updateUser(eq("ET001"), any(User.class));
    }

    @Test
    void testGetEtudiant_Success() {
        // Arrange
        EtudiantEntity entity = new EtudiantEntity();
        entity.setId("ET001");
        entity.setPhoneNumber("1234567890");
        entity.setEmail("etudiant@example.com");

        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("ET001");
        userRep.setEmail("etudiant@example.com");
        userRep.setUsername("etudiant@example.com");

        EtudiantDTO dto = new EtudiantDTO();
        dto.setId("ET001");
        dto.setPhoneNumber("1234567890");
        dto.setUser(new User("etudiant@example.com", "etudiant@example.com", null, null, null));

        when(etudiantRepository.findById("ET001")).thenReturn(Optional.of(entity));
        when(keycloakService.getUser("etudiant@example.com")).thenReturn(List.of(userRep));
        when(etudiantMapper.toEtudiantDTO(entity)).thenReturn(dto);
        when(etudiantMapper.userRepresentationToUser(userRep)).thenReturn(dto.getUser());

        // Act
        EtudiantDTO result = etudiantService.getEtudiant("ET001");

        // Assert
        assertEquals("ET001", result.getId(), "L'ID devrait être 'ET001'");
        verify(etudiantRepository, times(1)).findById("ET001");
        verify(keycloakService, times(1)).getUser("etudiant@example.com");
    }

    @Test
    void testGetAllEtudiant_Success() {
        // Arrange
        EtudiantEntity entity1 = new EtudiantEntity();
        entity1.setId("ET001");
        entity1.setPhoneNumber("1234567890");
        entity1.setEmail("etudiant1@example.com");
        entity1.setArchive(false);

        EtudiantEntity entity2 = new EtudiantEntity();
        entity2.setId("ET002");
        entity2.setPhoneNumber("0987654321");
        entity2.setEmail("etudiant2@example.com");
        entity2.setArchive(false);

        UserRepresentation userRep1 = new UserRepresentation();
        userRep1.setId("ET001");
        userRep1.setEmail("etudiant1@example.com");
        userRep1.setUsername("etudiant1@example.com");

        UserRepresentation userRep2 = new UserRepresentation();
        userRep2.setId("ET002");
        userRep2.setEmail("etudiant2@example.com");
        userRep2.setUsername("etudiant2@example.com");

        EtudiantDTO dto1 = new EtudiantDTO();
        dto1.setId("ET001");
        dto1.setPhoneNumber("1234567890");
        dto1.setUser(new User("etudiant1@example.com", "etudiant1@example.com", null, null, null));

        EtudiantDTO dto2 = new EtudiantDTO();
        dto2.setId("ET002");
        dto2.setPhoneNumber("0987654321");
        dto2.setUser(new User("etudiant2@example.com", "etudiant2@example.com", null, null, null));

        when(etudiantRepository.findAllByArchiveIsFalse()).thenReturn(Arrays.asList(entity1, entity2));
        when(keycloakService.getUser("etudiant1@example.com")).thenReturn(List.of(userRep1));
        when(keycloakService.getUser("etudiant2@example.com")).thenReturn(List.of(userRep2));
        when(etudiantMapper.toEtudiantDTO(entity1)).thenReturn(dto1);
        when(etudiantMapper.toEtudiantDTO(entity2)).thenReturn(dto2);
        when(etudiantMapper.userRepresentationToUser(userRep1)).thenReturn(dto1.getUser());
        when(etudiantMapper.userRepresentationToUser(userRep2)).thenReturn(dto2.getUser());

        // Act
        List<EtudiantDTO> result = etudiantService.getAllEtudiant();

        // Assert
        assertEquals(2, result.size(), "Il devrait y avoir 2 étudiants");
        verify(etudiantRepository, times(1)).findAllByArchiveIsFalse();
        verify(keycloakService, times(1)).getUser("etudiant1@example.com");
        verify(keycloakService, times(1)).getUser("etudiant2@example.com");
    }

    @Test
    void testDeleteEtudiant_Success() {
        // Arrange
        EtudiantEntity entity = new EtudiantEntity();
        entity.setId("ET001");
        entity.setArchive(false);

        EtudiantEntity archivedEntity = new EtudiantEntity();
        archivedEntity.setId("ET001");
        archivedEntity.setArchive(true);

        when(etudiantRepository.findById("ET001")).thenReturn(Optional.of(entity));
        when(etudiantRepository.save(any(EtudiantEntity.class))).thenReturn(archivedEntity);

        // Act
        etudiantService.deleteEtudiant("ET001");

        // Assert
        verify(etudiantRepository, times(1)).findById("ET001");
        verify(etudiantRepository, times(1)).save(any(EtudiantEntity.class));
        assertTrue(archivedEntity.getArchive(), "L'étudiant devrait être archivé");
    }

    @Test
    void testValidateEmailUniqueness_EmailExists() {
        // Arrange
        String email = "etudiant@example.com";
        EtudiantEntity existingEtudiant = new EtudiantEntity();
        existingEtudiant.setEmail(email);

        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(existingEtudiant));

        // Act & Assert
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                etudiantService.validateEmailUniqueness(email, null));
        assertEquals("Message par défaut", exception.getMessage(), "L'exception devrait contenir un message par défaut");
    }

    @Test
    void testGetEtudiant_NotFound() {
        // Arrange
        String id = "ET999";
        when(etudiantRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                etudiantService.getEtudiant(id));
        assertEquals("Message par défaut", exception.getMessage(), "L'exception devrait contenir un message par défaut");
    }
}