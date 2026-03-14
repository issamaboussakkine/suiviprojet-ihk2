package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/phases")
@CrossOrigin(origins = "*")
public class PhaseController {

    @Autowired
    private PhaseService phaseService;

    // Récupérer toutes les phases
    @GetMapping
    public List<Phase> getAllPhases() {
        return phaseService.getAllPhases();
    }

    // Récupérer une phase par ID
    @GetMapping("/{id}")
    public ResponseEntity<Phase> getPhaseById(@PathVariable Long id) {
        return phaseService.getPhaseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer une nouvelle phase
    @PostMapping
    public ResponseEntity<Phase> createPhase(@RequestBody Phase phase) {
        Phase saved = phaseService.savePhase(phase);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Modifier une phase
    @PutMapping("/{id}")
    public ResponseEntity<Phase> updatePhase(@PathVariable Long id, @RequestBody Phase phase) {
        Phase updated = phaseService.updatePhase(id, phase);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Supprimer une phase
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable Long id) {
        phaseService.deletePhase(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer les phases d'un projet
    @GetMapping("/projet/{projetId}")
    public List<Phase> getPhasesByProjet(@PathVariable Long projetId) {
        return phaseService.getPhasesByProjet(projetId);
    }

    // Récupérer les phases terminées mais non facturées (pour comptable)
    @GetMapping("/statut/terminees-non-facturees")
    public List<Phase> getPhasesTermineesNonFacturees() {
        return phaseService.getPhasesTermineesNonFacturees();
    }

    // Récupérer les phases facturées mais non payées (pour comptable)
    @GetMapping("/statut/facturees-non-payees")
    public List<Phase> getPhasesFactureesNonPayees() {
        return phaseService.getPhasesFactureesNonPayees();
    }

    // Récupérer les phases payées
    @GetMapping("/statut/payees")
    public List<Phase> getPhasesPayees() {
        return phaseService.getPhasesPayees();
    }

    // Récupérer les phases terminées d'un projet
    @GetMapping("/projet/{projetId}/terminees")
    public List<Phase> getPhasesTermineesByProjet(@PathVariable Long projetId) {
        return phaseService.getPhasesTermineesByProjet(projetId);
    }

    // Marquer une phase comme terminée
    @PutMapping("/{id}/terminer")
    public ResponseEntity<Phase> marquerTerminee(@PathVariable Long id) {
        Phase phase = phaseService.marquerTerminee(id);
        return phase != null ? ResponseEntity.ok(phase) : ResponseEntity.notFound().build();
    }

    // Marquer une phase comme facturée
    @PutMapping("/{id}/facturer")
    public ResponseEntity<Phase> marquerFacturee(@PathVariable Long id) {
        Phase phase = phaseService.marquerFacturee(id);
        return phase != null ? ResponseEntity.ok(phase) : ResponseEntity.notFound().build();
    }

    // Marquer une phase comme payée
    @PutMapping("/{id}/payer")
    public ResponseEntity<Phase> marquerPayee(@PathVariable Long id) {
        Phase phase = phaseService.marquerPayee(id);
        return phase != null ? ResponseEntity.ok(phase) : ResponseEntity.notFound().build();
    }

    // Obtenir le montant total des phases d'un projet
    @GetMapping("/projet/{projetId}/montant-total")
    public ResponseEntity<Double> getMontantTotalByProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(phaseService.getMontantTotalPhasesByProjet(projetId));
    }

    // Compter les phases en cours d'un projet
    @GetMapping("/projet/{projetId}/en-cours")
    public ResponseEntity<Long> countPhasesEnCoursByProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(phaseService.countPhasesEnCoursByProjet(projetId));
    }
}