package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.LivrableDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import ma.projet.ihk.suiviprojet_ihk.exception.LivrableNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.LivrableMapper;
import ma.projet.ihk.suiviprojet_ihk.service.LivrableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livrables")
@CrossOrigin(origins = "*")
public class LivrableController {

    @Autowired
    private LivrableService livrableService;

    @Autowired
    private LivrableMapper livrableMapper;

    // GET tous les livrables
    @GetMapping
    public List<LivrableDTO> getAllLivrables() {
        List<Livrable> livrables = livrableService.getAllLivrables();
        return livrables.stream()
                .map(livrableMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<LivrableDTO> getLivrableById(@PathVariable Long id) {
        Livrable livrable = livrableService.getLivrableById(id)
                .orElseThrow(() -> new LivrableNotFoundException("Livrable non trouvé avec l'ID: " + id));
        return ResponseEntity.ok(livrableMapper.toDto(livrable));
    }

    // POST créer un livrable
    @PostMapping
    public ResponseEntity<LivrableDTO> createLivrable(@RequestBody LivrableDTO dto) {
        Livrable livrable = livrableMapper.toEntity(dto);
        Livrable saved = livrableService.saveLivrable(livrable);
        return new ResponseEntity<>(livrableMapper.toDto(saved), HttpStatus.CREATED);
    }

    // PUT modifier un livrable
    @PutMapping("/{id}")
    public ResponseEntity<LivrableDTO> updateLivrable(@PathVariable Long id, @RequestBody LivrableDTO dto) {
        Livrable existingLivrable = livrableService.getLivrableById(id)
                .orElseThrow(() -> new LivrableNotFoundException("Livrable non trouvé avec l'ID: " + id));

        livrableMapper.updateEntityFromDto(dto, existingLivrable);
        Livrable updated = livrableService.saveLivrable(existingLivrable);
        return ResponseEntity.ok(livrableMapper.toDto(updated));
    }

    // DELETE supprimer un livrable
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivrable(@PathVariable Long id) {
        livrableService.deleteLivrable(id);
        return ResponseEntity.noContent().build();
    }

    // GET livrables par phase
    @GetMapping("/phase/{phaseId}")
    public List<LivrableDTO> getLivrablesByPhase(@PathVariable Long phaseId) {
        List<Livrable> livrables = livrableService.getLivrablesByPhase(phaseId);
        return livrables.stream()
                .map(livrableMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET count livrables par phase
    @GetMapping("/phase/{phaseId}/count")
    public ResponseEntity<Long> countLivrablesByPhase(@PathVariable Long phaseId) {
        return ResponseEntity.ok(livrableService.countLivrablesByPhase(phaseId));
    }

    // GET vérifier si phase a des livrables
    @GetMapping("/phase/{phaseId}/exists")
    public ResponseEntity<Boolean> hasLivrables(@PathVariable Long phaseId) {
        return ResponseEntity.ok(livrableService.hasLivrables(phaseId));
    }
}