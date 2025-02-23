package com.groupeisi.services.controller;

import com.groupeisi.services.dto.ProfesseurDTO;
import com.groupeisi.services.services.ProfesseurService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/professeurs")
@AllArgsConstructor
public class ProfesseurController {

    private final ProfesseurService professeurService;

    @PostMapping
    public ProfesseurDTO addProfesseur(@RequestBody ProfesseurDTO request) {
        return professeurService.addProfesseur(request);
    }

    @PutMapping("/{id}")
    public ProfesseurDTO updateProfesseur(@PathVariable("id") String id, @RequestBody ProfesseurDTO request) {
        return professeurService.updateProfesseur(id, request);
    }

    @GetMapping("/{id}")
    public ProfesseurDTO getProfesseur(@PathVariable("id") String id) {
        return professeurService.getProfesseur(id);
    }

    @GetMapping()
    public List<ProfesseurDTO> getAllProfesseur() {
        return professeurService.getAllProfesseur();
    }

    @DeleteMapping("/{id}")
    public void deleteProfesseur(@PathVariable("id") String id) {
        professeurService.deleteProfesseur(id);
    }
}