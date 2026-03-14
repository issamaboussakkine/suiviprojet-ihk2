package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import ma.projet.ihk.suiviprojet_ihk.service.LivrableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/livrables")
@CrossOrigin(origins = "*")
public class LivrableController {

    @Autowired
    private LivrableService livrableService;

    // Récupérer tous les livrables
    @GetMapping
    public List<Livrable> getAllLivrables() {
        return livrableService.getAllLivrables();
    }

    // Récupérer un livrable par ID
    @GetMapping("/{id}")
    public ResponseEntity<Livrable> getLivrableById(@PathVariable Long id) {
        return livrableService.getLivrableById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouveau livrable
    @PostMapping
    public ResponseEntity<Livrable> createLivrable(@RequestBody Livrable livrable) {
        Livrable saved = livrableService.saveLivrable(livrable);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Modifier un livrable
    @PutMapping("/{id}")
    public ResponseEntity<Livrable> updateLivrable(@PathVariable Long id, @RequestBody Livrable livrable) {
        Livrable updated = livrableService.updateLivrable(id, livrable);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Supprimer un livrable
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivrable(@PathVariable Long id) {
        livrableService.deleteLivrable(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer les livrables d'une phase
    @GetMapping("/phase/{phaseId}")
    public List<Livrable> getLivrablesByPhase(@PathVariable Long phaseId) {
        return livrableService.getLivrablesByPhase(phaseId);
    }

    // Compter les livrables d'une phase
    @GetMapping("/phase/{phaseId}/count")
    public ResponseEntity<Long> countLivrablesByPhase(@PathVariable Long phaseId) {
        return ResponseEntity.ok(livrableService.countLivrablesByPhase(phaseId));
    }

    // Vérifier si une phase a des livrables
    @GetMapping("/phase/{phaseId}/exists")
    public ResponseEntity<Boolean> hasLivrables(@PathVariable Long phaseId) {
        return ResponseEntity.ok(livrableService.hasLivrables(phaseId));
    }
}