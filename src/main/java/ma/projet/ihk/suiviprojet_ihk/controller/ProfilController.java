package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.ProfilDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import ma.projet.ihk.suiviprojet_ihk.exception.ProfilNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.ProfilMapper;
import ma.projet.ihk.suiviprojet_ihk.service.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profils")
@CrossOrigin(origins = "*")
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    @Autowired
    private ProfilMapper profilMapper;

    // GET tous les profils
    @GetMapping
    public List<ProfilDTO> getAllProfils() {
        List<Profil> profils = profilService.getAllProfils();
        return profils.stream()
                .map(profilMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfilDTO> getProfilById(@PathVariable Long id) {
        Profil profil = profilService.getProfilById(id)
                .orElseThrow(() -> new ProfilNotFoundException("Profil non trouvé avec l'ID: " + id));
        return ResponseEntity.ok(profilMapper.toDto(profil));
    }

    // GET par code
    @GetMapping("/code/{code}")
    public ResponseEntity<ProfilDTO> getProfilByCode(@PathVariable String code) {
        Profil profil = profilService.getProfilByCode(code)
                .orElseThrow(() -> new RuntimeException("Profil non trouvé avec code: " + code));
        return ResponseEntity.ok(profilMapper.toDto(profil));
    }

    // POST créer un profil
    @PostMapping
    public ResponseEntity<ProfilDTO> createProfil(@RequestBody ProfilDTO dto) {
        if (!profilService.isCodeUnique(dto.getCode())) {
            return ResponseEntity.badRequest().build();
        }
        Profil profil = profilMapper.toEntity(dto);
        Profil saved = profilService.saveProfil(profil);
        return new ResponseEntity<>(profilMapper.toDto(saved), HttpStatus.CREATED);
    }

    // PUT modifier un profil
    @PutMapping("/{id}")
    public ResponseEntity<ProfilDTO> updateProfil(@PathVariable Long id, @RequestBody ProfilDTO dto) {
        Profil existingProfil = profilService.getProfilById(id)
                .orElseThrow(() -> new ProfilNotFoundException("Profil non trouvé avec l'ID: " + id));

        profilMapper.updateEntityFromDto(dto, existingProfil);
        Profil updated = profilService.saveProfil(existingProfil);
        return ResponseEntity.ok(profilMapper.toDto(updated));
    }

    // DELETE supprimer un profil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfil(@PathVariable Long id) {
        profilService.deleteProfil(id);
        return ResponseEntity.noContent().build();
    }

    // GET tous les codes de profils
    @GetMapping("/codes")
    public ResponseEntity<List<String>> getAllCodes() {
        return ResponseEntity.ok(profilService.getAllCodes());
    }
}