package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.AffectationDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import ma.projet.ihk.suiviprojet_ihk.exception.AffectationNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.AffectationMapper;
import ma.projet.ihk.suiviprojet_ihk.service.AffectationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/affectations")
@CrossOrigin(origins = "*")
public class AffectationController {

    @Autowired
    private AffectationService affectationService;

    @Autowired
    private AffectationMapper affectationMapper;

    // GET toutes les affectations
    @GetMapping
    public List<AffectationDTO> getAllAffectations() {
        List<Affectation> affectations = affectationService.getAllAffectations();
        return affectations.stream()
                .map(affectationMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID composé
    @GetMapping("/{employeId}/{phaseId}")
    public ResponseEntity<AffectationDTO> getAffectationById(
            @PathVariable int employeId,
            @PathVariable int phaseId) {
        AffectationId id = new AffectationId(employeId, phaseId);
        Affectation affectation = affectationService.getAffectationById(id)
                .orElseThrow(() -> new AffectationNotFoundException("Affectation non trouvée"));
        return ResponseEntity.ok(affectationMapper.toDto(affectation));
    }

    // POST créer une affectation
    @PostMapping
    public ResponseEntity<AffectationDTO> createAffectation(@RequestBody AffectationDTO dto) {
        try {
            Affectation affectation = affectationMapper.toEntity(dto);
            Affectation saved = affectationService.saveAffectation(affectation);
            return new ResponseEntity<>(affectationMapper.toDto(saved), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT modifier une affectation
    @PutMapping("/{employeId}/{phaseId}")
    public ResponseEntity<AffectationDTO> updateAffectation(
            @PathVariable int employeId,
            @PathVariable int phaseId,
            @RequestBody AffectationDTO dto) {
        AffectationId id = new AffectationId(employeId, phaseId);
        Affectation existingAffectation = affectationService.getAffectationById(id)
                .orElseThrow(() -> new AffectationNotFoundException("Affectation non trouvée"));

        affectationMapper.updateEntityFromDto(dto, existingAffectation);
        Affectation updated = affectationService.saveAffectation(existingAffectation);
        return ResponseEntity.ok(affectationMapper.toDto(updated));
    }

    // DELETE supprimer une affectation
    @DeleteMapping("/{employeId}/{phaseId}")
    public ResponseEntity<Void> deleteAffectation(
            @PathVariable int employeId,
            @PathVariable int phaseId) {
        AffectationId id = new AffectationId(employeId, phaseId);
        affectationService.deleteAffectation(id);
        return ResponseEntity.noContent().build();
    }

    // GET affectations par phase
    @GetMapping("/phase/{phaseId}")
    public List<AffectationDTO> getAffectationsByPhase(@PathVariable Long phaseId) {
        List<Affectation> affectations = affectationService.getAffectationsByPhase(phaseId);
        return affectations.stream()
                .map(affectationMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET affectations par employé
    @GetMapping("/employe/{employeId}")
    public List<AffectationDTO> getAffectationsByEmploye(@PathVariable Long employeId) {
        List<Affectation> affectations = affectationService.getAffectationsByEmploye(employeId);
        return affectations.stream()
                .map(affectationMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET vérifier disponibilité employé
    @GetMapping("/disponible/{employeId}")
    public ResponseEntity<Boolean> isEmployeDisponible(
            @PathVariable Long employeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(affectationService.isEmployeDisponible(employeId, dateDebut, dateFin));
    }
}