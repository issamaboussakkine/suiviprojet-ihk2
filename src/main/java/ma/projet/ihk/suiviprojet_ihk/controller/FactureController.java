package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.FactureDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import ma.projet.ihk.suiviprojet_ihk.exception.FactureNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.FactureMapper;
import ma.projet.ihk.suiviprojet_ihk.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/factures")
@CrossOrigin(origins = "*")
public class FactureController {

    @Autowired
    private FactureService factureService;

    @Autowired
    private FactureMapper factureMapper;

    // GET toutes les factures
    @GetMapping
    public List<FactureDTO> getAllFactures() {
        List<Facture> factures = factureService.getAllFactures();
        return factures.stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<FactureDTO> getFactureById(@PathVariable Long id) {
        Facture facture = factureService.getFactureById(id)
                .orElseThrow(() -> new FactureNotFoundException("Facture non trouvée avec l'ID: " + id));
        return ResponseEntity.ok(factureMapper.toDto(facture));
    }

    // POST créer une facture
    @PostMapping
    public ResponseEntity<FactureDTO> createFacture(@RequestBody FactureDTO dto) {
        try {
            Facture facture = factureMapper.toEntity(dto);
            Facture saved = factureService.saveFacture(facture);
            return new ResponseEntity<>(factureMapper.toDto(saved), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT modifier une facture
    @PutMapping("/{id}")
    public ResponseEntity<FactureDTO> updateFacture(@PathVariable Long id, @RequestBody FactureDTO dto) {
        Facture existingFacture = factureService.getFactureById(id)
                .orElseThrow(() -> new FactureNotFoundException("Facture non trouvée avec l'ID: " + id));

        factureMapper.updateEntityFromDto(dto, existingFacture);
        Facture updated = factureService.saveFacture(existingFacture);
        return ResponseEntity.ok(factureMapper.toDto(updated));
    }

    // DELETE supprimer une facture
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        factureService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }

    // GET facture par phase
    @GetMapping("/phase/{phaseId}")
    public ResponseEntity<FactureDTO> getFactureByPhase(@PathVariable Long phaseId) {
        return factureService.getFactureByPhase(phaseId)
                .map(facture -> ResponseEntity.ok(factureMapper.toDto(facture)))
                .orElse(ResponseEntity.notFound().build());
    }

    // GET factures par période
    @GetMapping("/periode")
    public List<FactureDTO> getFacturesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Facture> factures = factureService.getFacturesByPeriode(dateDebut, dateFin);
        return factures.stream()
                .map(factureMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET vérifier si phase est facturée
    @GetMapping("/phase/{phaseId}/existe")
    public ResponseEntity<Boolean> phaseDejaFacturee(@PathVariable Long phaseId) {
        return ResponseEntity.ok(factureService.phaseDejaFacturee(phaseId));
    }

    // GET count factures par période
    @GetMapping("/periode/count")
    public ResponseEntity<Long> countFacturesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(factureService.countFacturesByPeriode(dateDebut, dateFin));
    }
}