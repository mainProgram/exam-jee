package com.groupeisi.services.controller;


import com.groupeisi.services.dto.EtudiantDTO;
import com.groupeisi.services.services.EtudiantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/etudiants")
@AllArgsConstructor
public class EtudiantController {

    EtudiantService etudiantService;

    @PostMapping
    public EtudiantDTO addEtudiant(@RequestBody EtudiantDTO request) {
        return etudiantService.addEtudiant(request);
    }
}
