package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import ma.projet.ihk.suiviprojet_ihk.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
@CrossOrigin(origins = "*")
public class FactureController {

    @Autowired
    private FactureService factureService;

    // Récupérer toutes les factures
    @GetMapping
    public List<Facture> getAllFactures() {
        return factureService.getAllFactures();
    }

    // Récupérer une facture par ID
    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFactureById(@PathVariable Long id) {
        return factureService.getFactureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer une nouvelle facture
    @PostMapping
    public ResponseEntity<Facture> createFacture(@RequestBody Facture facture) {
        try {
            Facture saved = factureService.saveFacture(facture);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Modifier une facture
    @PutMapping("/{id}")
    public ResponseEntity<Facture> updateFacture(@PathVariable Long id, @RequestBody Facture facture) {
        Facture updated = factureService.updateFacture(id, facture);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Supprimer une facture
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        factureService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer la facture d'une phase
    @GetMapping("/phase/{phaseId}")
    public ResponseEntity<Facture> getFactureByPhase(@PathVariable Long phaseId) {
        return factureService.getFactureByPhase(phaseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Rechercher les factures par période
    @GetMapping("/periode")
    public List<Facture> getFacturesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return factureService.getFacturesByPeriode(dateDebut, dateFin);
    }

    // Vérifier si une phase est déjà facturée
    @GetMapping("/phase/{phaseId}/existe")
    public ResponseEntity<Boolean> phaseDejaFacturee(@PathVariable Long phaseId) {
        return ResponseEntity.ok(factureService.phaseDejaFacturee(phaseId));
    }

    // Compter les factures sur une période
    @GetMapping("/periode/count")
    public ResponseEntity<Long> countFacturesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(factureService.countFacturesByPeriode(dateDebut, dateFin));
    }
}