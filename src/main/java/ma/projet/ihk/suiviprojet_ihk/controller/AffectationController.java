package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import ma.projet.ihk.suiviprojet_ihk.service.AffectationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/affectations")
@CrossOrigin(origins = "*")
public class AffectationController {

    @Autowired
    private AffectationService affectationService;

    // Récupérer toutes les affectations
    @GetMapping
    public List<Affectation> getAllAffectations() {
        return affectationService.getAllAffectations();
    }

    // Récupérer une affectation par sa clé composée
    @GetMapping("/{employeId}/{phaseId}")
    public ResponseEntity<Affectation> getAffectationById(
            @PathVariable int employeId,
            @PathVariable int phaseId) {
        AffectationId id = new AffectationId(employeId, phaseId);
        return affectationService.getAffectationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer une nouvelle affectation
    @PostMapping
    public ResponseEntity<Affectation> createAffectation(@RequestBody Affectation affectation) {
        try {
            Affectation saved = affectationService.saveAffectation(affectation);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Modifier une affectation
    @PutMapping("/{employeId}/{phaseId}")
    public ResponseEntity<Affectation> updateAffectation(
            @PathVariable int employeId,
            @PathVariable int phaseId,
            @RequestBody Affectation affectation) {
        AffectationId id = new AffectationId(employeId, phaseId);
        Affectation updated = affectationService.updateAffectation(id, affectation);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Supprimer une affectation
    @DeleteMapping("/{employeId}/{phaseId}")
    public ResponseEntity<Void> deleteAffectation(
            @PathVariable int employeId,
            @PathVariable int phaseId) {
        AffectationId id = new AffectationId(employeId, phaseId);
        affectationService.deleteAffectation(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer les affectations d'une phase
    @GetMapping("/phase/{phaseId}")
    public List<Affectation> getAffectationsByPhase(@PathVariable Long phaseId) {
        return affectationService.getAffectationsByPhase(phaseId);
    }

    // Récupérer les affectations d'un employé
    @GetMapping("/employe/{employeId}")
    public List<Affectation> getAffectationsByEmploye(@PathVariable Long employeId) {
        return affectationService.getAffectationsByEmploye(employeId);
    }

    // Vérifier si un employé est affecté à une phase
    @GetMapping("/existe/{employeId}/{phaseId}")
    public ResponseEntity<Boolean> isEmployeAffectePhase(
            @PathVariable Long employeId,
            @PathVariable Long phaseId) {
        return ResponseEntity.ok(affectationService.isEmployeAffectePhase(employeId, phaseId));
    }

    // Vérifier si un employé est disponible sur une période
    @GetMapping("/disponible/{employeId}")
    public ResponseEntity<Boolean> isEmployeDisponible(
            @PathVariable Long employeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(affectationService.isEmployeDisponible(employeId, dateDebut, dateFin));
    }

    // Compter les employés sur une phase
    @GetMapping("/phase/{phaseId}/count-employes")
    public ResponseEntity<Long> countEmployesByPhase(@PathVariable Long phaseId) {
        return ResponseEntity.ok(affectationService.countEmployesByPhase(phaseId));
    }

    // Compter les phases d'un employé
    @GetMapping("/employe/{employeId}/count-phases")
    public ResponseEntity<Long> countPhasesByEmploye(@PathVariable Long employeId) {
        return ResponseEntity.ok(affectationService.countPhasesByEmploye(employeId));
    }
}