package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import ma.projet.ihk.suiviprojet_ihk.service.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profils")
@CrossOrigin(origins = "*")
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    // Récupérer tous les profils
    @GetMapping
    public List<Profil> getAllProfils() {
        return profilService.getAllProfils();
    }

    // Récupérer un profil par ID
    @GetMapping("/{id}")
    public ResponseEntity<Profil> getProfilById(@PathVariable Long id) {
        return profilService.getProfilById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer un profil par code
    @GetMapping("/code/{code}")
    public ResponseEntity<Profil> getProfilByCode(@PathVariable String code) {
        return profilService.getProfilByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouveau profil
    @PostMapping
    public ResponseEntity<Profil> createProfil(@RequestBody Profil profil) {
        // Vérifier si le code est unique
        if (!profilService.isCodeUnique(profil.getCode())) {
            return ResponseEntity.badRequest().build();
        }
        Profil saved = profilService.saveProfil(profil);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Modifier un profil
    @PutMapping("/{id}")
    public ResponseEntity<Profil> updateProfil(@PathVariable Long id, @RequestBody Profil profil) {
        Profil updated = profilService.updateProfil(id, profil);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Supprimer un profil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfil(@PathVariable Long id) {
        profilService.deleteProfil(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer tous les codes de profils
    @GetMapping("/codes")
    public ResponseEntity<List<String>> getAllCodes() {
        return ResponseEntity.ok(profilService.getAllCodes());
    }
}