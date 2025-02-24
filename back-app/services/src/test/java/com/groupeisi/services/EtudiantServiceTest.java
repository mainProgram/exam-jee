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
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceTest {

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private IEtudiantRepository etudiantRepository;

    @Mock
    private EtudiantMapper etudiantMapper;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private EtudiantService etudiantService;

    private EtudiantDTO etudiantDTO;
    private EtudiantEntity etudiantEntity;
    private User user;
    private UserRepresentation userRepresentation;

    @BeforeEach
    void setUp() {
         user = new User();
        user.setEmailId("test@example.com");
        user.setUserName("test@example.com");
        user.setFirstname("fz");
        user.setLastName("fd");

        etudiantDTO = new EtudiantDTO();
        etudiantDTO.setUser(user);
        etudiantDTO.setPhoneNumber("772427889");
        etudiantDTO.setAddress("Biagui");
        etudiantDTO.setArchive(false);

        etudiantEntity = new EtudiantEntity();
        etudiantEntity.setId("123e4567-e89b-12d3-a456-426614174000");
        etudiantEntity.setEmail("test@example.com");
        etudiantEntity.setPhoneNumber("772427889");
        etudiantEntity.setAddress("Biagui");
        etudiantEntity.setArchive(false);
        etudiantEntity.setRegistrationNumber("SN-MAT-1");

        userRepresentation = new UserRepresentation();
        userRepresentation.setId("123e4567-e89b-12d3-a456-426614174000");
        userRepresentation.setEmail("test@example.com");
        userRepresentation.setUsername("test@example.com");
        userRepresentation.setFirstName("fz");
        userRepresentation.setLastName("fd");
        userRepresentation.setEnabled(true);
    }

    @Test
    void testAddEtudiant_Success() {
        when(etudiantRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(keycloakService.getUser(anyString())).thenReturn(List.of(userRepresentation));
        when(etudiantMapper.toEntity(any(EtudiantDTO.class))).thenReturn(etudiantEntity);
        when(etudiantRepository.save(any(EtudiantEntity.class))).thenReturn(etudiantEntity);
        when(etudiantMapper.toEtudiantDTO(any(EtudiantEntity.class))).thenReturn(etudiantDTO);
        when(etudiantMapper.userRepresentationToUser(any(UserRepresentation.class))).thenReturn(user);
        when(etudiantRepository.findAll()).thenReturn(new ArrayList<>());

        EtudiantDTO result = etudiantService.addEtudiant(etudiantDTO);

        assertNotNull(result);
        verify(keycloakService).addUser(any(User.class));
        verify(etudiantRepository).save(any(EtudiantEntity.class));
    }

    @Test
    void testAddEtudiant_EmailExists() {
        when(etudiantRepository.findByEmail(anyString())).thenReturn(Optional.of(etudiantEntity));
        when(messageSource.getMessage(eq("etudiant.exists"), any(), any(Locale.class))).thenReturn("Email exists");

        assertThrows(EntityExistsException.class, () -> etudiantService.addEtudiant(etudiantDTO));
        verify(keycloakService, never()).addUser(any(User.class));
        verify(etudiantRepository, never()).save(any(EtudiantEntity.class));
    }

    @Test
    void testUpdateEtudiant_Success() {
        String id = "123e4567-e89b-12d3-a456-426614174000";
        when(etudiantRepository.findById(id)).thenReturn(Optional.of(etudiantEntity));
        when(etudiantRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(keycloakService.getUser(anyString())).thenReturn(List.of(userRepresentation));
        when(etudiantRepository.save(any(EtudiantEntity.class))).thenReturn(etudiantEntity);
        when(etudiantMapper.toEtudiantDTO(any(EtudiantEntity.class))).thenReturn(etudiantDTO);
        when(etudiantMapper.userRepresentationToUser(any(UserRepresentation.class))).thenReturn(user);

        EtudiantDTO result = etudiantService.updateEtudiant(id, etudiantDTO);

        assertNotNull(result);
        verify(keycloakService).updateUser(eq(id), any(User.class));
        verify(etudiantRepository).save(any(EtudiantEntity.class));
    }

    @Test
    void testUpdateEtudiant_NotFound() {
        String id = "nonexistent-id";
        when(etudiantRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("etudiant.notfound"), any(), any(Locale.class))).thenReturn("Etudiant not found");

        assertThrows(EntityNotFoundException.class, () -> etudiantService.updateEtudiant(id, etudiantDTO));
        verify(keycloakService, never()).updateUser(anyString(), any(User.class));
        verify(etudiantRepository, never()).save(any(EtudiantEntity.class));
    }

    @Test
    void testGetEtudiant_Success() {
        String id = "123e4567-e89b-12d3-a456-426614174000";
        when(etudiantRepository.findById(id)).thenReturn(Optional.of(etudiantEntity));
        when(keycloakService.getUser(anyString())).thenReturn(List.of(userRepresentation));
        when(etudiantMapper.toEtudiantDTO(any(EtudiantEntity.class))).thenReturn(etudiantDTO);
        when(etudiantMapper.userRepresentationToUser(any(UserRepresentation.class))).thenReturn(user);

        EtudiantDTO result = etudiantService.getEtudiant(id);

        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void testGetEtudiant_NotFound() {
        String id = "nonexistent-id";
        when(etudiantRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("etudiant.notfound"), any(), any(Locale.class))).thenReturn("Etudiant not found");

        assertThrows(EntityNotFoundException.class, () -> etudiantService.getEtudiant(id));
    }

    @Test
    void testGetAllEtudiant_Success() {
        List<EtudiantEntity> etudiants = List.of(etudiantEntity);
        when(etudiantRepository.findAllByArchiveIsFalse()).thenReturn(etudiants);
        when(keycloakService.getUser(anyString())).thenReturn(List.of(userRepresentation));
        when(etudiantMapper.toEtudiantDTO(any(EtudiantEntity.class))).thenReturn(etudiantDTO);
        when(etudiantMapper.userRepresentationToUser(any(UserRepresentation.class))).thenReturn(user);

        List<EtudiantDTO> results = etudiantService.getAllEtudiant();

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void testDeleteEtudiant_Success() {
        String id = "123e4567-e89b-12d3-a456-426614174000";
        when(etudiantRepository.findById(id)).thenReturn(Optional.of(etudiantEntity));

        etudiantService.deleteEtudiant(id);

        assertTrue(etudiantEntity.getArchive());
        verify(etudiantRepository).save(etudiantEntity);
    }

    @Test
    void testDeleteEtudiant_NotFound() {
        String id = "nonexistent-id";
        when(etudiantRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("etudiant.notfound"), any(), any(Locale.class))).thenReturn("Etudiant not found");

        assertThrows(EntityNotFoundException.class, () -> etudiantService.deleteEtudiant(id));
        verify(etudiantRepository, never()).save(any(EtudiantEntity.class));
    }

}