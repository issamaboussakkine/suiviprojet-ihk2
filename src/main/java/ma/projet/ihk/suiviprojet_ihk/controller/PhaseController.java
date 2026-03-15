package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.PhaseDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.exception.PhaseNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.PhaseMapper;
import ma.projet.ihk.suiviprojet_ihk.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/phases")
@CrossOrigin(origins = "*")
public class PhaseController {

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private PhaseMapper phaseMapper;

    // GET toutes les phases
    @GetMapping
    public List<PhaseDTO> getAllPhases() {
        List<Phase> phases = phaseService.getAllPhases();
        return phases.stream()
                .map(phaseMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<PhaseDTO> getPhaseById(@PathVariable Long id) {
        Phase phase = phaseService.getPhaseById(id)
                .orElseThrow(() -> new PhaseNotFoundException("Phase non trouvée avec l'ID: " + id));
        return ResponseEntity.ok(phaseMapper.toDto(phase));
    }

    // POST créer une phase
    @PostMapping
    public ResponseEntity<PhaseDTO> createPhase(@RequestBody PhaseDTO dto) {
        Phase phase = phaseMapper.toEntity(dto);
        Phase saved = phaseService.savePhase(phase);
        return new ResponseEntity<>(phaseMapper.toDto(saved), HttpStatus.CREATED);
    }

    // PUT modifier une phase
    @PutMapping("/{id}")
    public ResponseEntity<PhaseDTO> updatePhase(@PathVariable Long id, @RequestBody PhaseDTO dto) {
        Phase existingPhase = phaseService.getPhaseById(id)
                .orElseThrow(() -> new PhaseNotFoundException("Phase non trouvée avec l'ID: " + id));

        phaseMapper.updateEntityFromDto(dto, existingPhase);
        Phase updated = phaseService.savePhase(existingPhase);
        return ResponseEntity.ok(phaseMapper.toDto(updated));
    }

    // DELETE supprimer une phase
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable Long id) {
        phaseService.deletePhase(id);
        return ResponseEntity.noContent().build();
    }

    // GET phases par projet
    @GetMapping("/projet/{projetId}")
    public List<PhaseDTO> getPhasesByProjet(@PathVariable Long projetId) {
        List<Phase> phases = phaseService.getPhasesByProjet(projetId);
        return phases.stream()
                .map(phaseMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET phases terminées non facturées
    @GetMapping("/statut/terminees-non-facturees")
    public List<PhaseDTO> getPhasesTermineesNonFacturees() {
        List<Phase> phases = phaseService.getPhasesTermineesNonFacturees();
        return phases.stream()
                .map(phaseMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET phases facturées non payées
    @GetMapping("/statut/facturees-non-payees")
    public List<PhaseDTO> getPhasesFactureesNonPayees() {
        List<Phase> phases = phaseService.getPhasesFactureesNonPayees();
        return phases.stream()
                .map(phaseMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET phases payées
    @GetMapping("/statut/payees")
    public List<PhaseDTO> getPhasesPayees() {
        List<Phase> phases = phaseService.getPhasesPayees();
        return phases.stream()
                .map(phaseMapper::toDto)
                .collect(Collectors.toList());
    }

    // PUT marquer phase comme terminée
    @PutMapping("/{id}/terminer")
    public ResponseEntity<PhaseDTO> marquerTerminee(@PathVariable Long id) {
        Phase phase = phaseService.marquerTerminee(id);
        if (phase != null) {
            return ResponseEntity.ok(phaseMapper.toDto(phase));
        }
        throw new PhaseNotFoundException("Phase non trouvée avec l'ID: " + id);
    }

    // PUT marquer phase comme facturée
    @PutMapping("/{id}/facturer")
    public ResponseEntity<PhaseDTO> marquerFacturee(@PathVariable Long id) {
        Phase phase = phaseService.marquerFacturee(id);
        if (phase != null) {
            return ResponseEntity.ok(phaseMapper.toDto(phase));
        }
        throw new PhaseNotFoundException("Phase non trouvée avec l'ID: " + id);
    }

    // PUT marquer phase comme payée
    @PutMapping("/{id}/payer")
    public ResponseEntity<PhaseDTO> marquerPayee(@PathVariable Long id) {
        Phase phase = phaseService.marquerPayee(id);
        if (phase != null) {
            return ResponseEntity.ok(phaseMapper.toDto(phase));
        }
        throw new PhaseNotFoundException("Phase non trouvée avec l'ID: " + id);
    }

    // GET montant total des phases d'un projet
    @GetMapping("/projet/{projetId}/montant-total")
    public ResponseEntity<Double> getMontantTotalByProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(phaseService.getMontantTotalPhasesByProjet(projetId));
    }

    // GET count phases en cours d'un projet
    @GetMapping("/projet/{projetId}/en-cours")
    public ResponseEntity<Long> countPhasesEnCoursByProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(phaseService.countPhasesEnCoursByProjet(projetId));
    }
}