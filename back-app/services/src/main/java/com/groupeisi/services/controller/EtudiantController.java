package com.groupeisi.services.controller;


import com.groupeisi.services.dto.EtudiantDTO;
import com.groupeisi.services.services.EtudiantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@AllArgsConstructor
public class EtudiantController {

    EtudiantService etudiantService;

    @PostMapping
    public EtudiantDTO addEtudiant(@RequestBody EtudiantDTO request) {
        return etudiantService.addEtudiant(request);
    }

    @PutMapping("/{id}")
    public EtudiantDTO updateEtudiant(@PathVariable("id") String id, @RequestBody EtudiantDTO request) {
        return etudiantService.updateEtudiant(id, request);
    }

    @GetMapping("/{id}")
    public EtudiantDTO getEtudiant(@PathVariable("id") String id) {
        return etudiantService.getEtudiant(id);
    }

    @GetMapping()
    public List<EtudiantDTO> getAllEtudiant() {
        return etudiantService.getAllEtudiant();
    }

    @DeleteMapping("/{id}")
    public void deleteEtudiant(@PathVariable("id") String id) {
        etudiantService.deleteEtudiant(id);
    }
}
