package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.ProjetDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.exception.ProjetNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.ProjetMapper;
import ma.projet.ihk.suiviprojet_ihk.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "*")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    @Autowired
    private ProjetMapper projetMapper;

    // GET tous les projets
    @GetMapping
    public List<ProjetDTO> getAllProjets() {
        List<Projet> projets = projetService.getAllProjets();
        return projets.stream()
                .map(projetMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjetDTO> getProjetById(@PathVariable Long id) {
        Projet projet = projetService.getProjetById(id)
                .orElseThrow(() -> new ProjetNotFoundException("Projet non trouvé avec l'ID: " + id));
        return ResponseEntity.ok(projetMapper.toDto(projet));
    }

    // GET par code
    @GetMapping("/code/{code}")
    public ResponseEntity<ProjetDTO> getProjetByCode(@PathVariable String code) {
        Projet projet = projetService.getProjetByCode(code)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec code: " + code));
        return ResponseEntity.ok(projetMapper.toDto(projet));
    }

    // POST créer un projet
    @PostMapping
    public ResponseEntity<ProjetDTO> createProjet(@RequestBody ProjetDTO dto) {
        Projet projet = projetMapper.toEntity(dto);
        Projet saved = projetService.saveProjet(projet);
        return new ResponseEntity<>(projetMapper.toDto(saved), HttpStatus.CREATED);
    }

    // PUT modifier un projet
    @PutMapping("/{id}")
    public ResponseEntity<ProjetDTO> updateProjet(@PathVariable Long id, @RequestBody ProjetDTO dto) {
        Projet existingProjet = projetService.getProjetById(id)
                .orElseThrow(() -> new ProjetNotFoundException("Projet non trouvé avec l'ID: " + id));

        projetMapper.updateEntityFromDto(dto, existingProjet);
        Projet updated = projetService.saveProjet(existingProjet);
        return ResponseEntity.ok(projetMapper.toDto(updated));
    }

    // DELETE supprimer un projet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        projetService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }

    // GET par organisme
    @GetMapping("/organisme/{organismeId}")
    public List<ProjetDTO> getProjetsByOrganisme(@PathVariable Long organismeId) {
        List<Projet> projets = projetService.getProjetsByOrganisme(organismeId);
        return projets.stream()
                .map(projetMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par chef de projet
    @GetMapping("/chef/{chefProjetId}")
    public List<ProjetDTO> getProjetsByChefProjet(@PathVariable Long chefProjetId) {
        List<Projet> projets = projetService.getProjetsByChefProjet(chefProjetId);
        return projets.stream()
                .map(projetMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET recherche par nom
    @GetMapping("/search")
    public List<ProjetDTO> searchProjetsByNom(@RequestParam String nom) {
        List<Projet> projets = projetService.searchProjetsByNom(nom);
        return projets.stream()
                .map(projetMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET montant total
    @GetMapping("/montant-total")
    public ResponseEntity<Double> getMontantTotal() {
        return ResponseEntity.ok(projetService.getMontantTotalProjets());
    }

    // GET count projets en cours
    @GetMapping("/count/en-cours")
    public ResponseEntity<Long> countProjetsEnCours() {
        return ResponseEntity.ok(projetService.countProjetsEnCours());
    }

    // GET count projets terminés
    @GetMapping("/count/termines")
    public ResponseEntity<Long> countProjetsTermines() {
        return ResponseEntity.ok(projetService.countProjetsTermines());
    }

    // ========== NOUVEAUX ENDPOINTS ==========

    // GET taux d'avancement d'un projet
    @GetMapping("/{id}/taux-avancement")
    public ResponseEntity<Integer> getTauxAvancement(@PathVariable Long id) {
        int taux = projetService.getTauxAvancement(id);
        return ResponseEntity.ok(taux);
    }

    // GET tous les projets avec leur taux d'avancement
    @GetMapping("/avec-taux")
    public List<ProjetDTO> getAllProjetsAvecTaux() {
        List<Projet> projets = projetService.getAllProjetsAvecTaux();
        return projets.stream()
                .map(projetMapper::toDto)
                .collect(Collectors.toList());
    }

    // PUT valider un projet (Directeur)
    @PutMapping("/{id}/valider")
    public ResponseEntity<Map<String, String>> validerProjet(@PathVariable Long id) {
        try {
            Projet projet = projetService.validerProjet(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Projet validé avec succès");
            response.put("statut", projet.getStatut());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // PUT démarrer un projet (Chef de projet)
    @PutMapping("/{id}/demarrer")
    public ResponseEntity<Map<String, String>> demarrerProjet(@PathVariable Long id) {
        try {
            Projet projet = projetService.demarrerProjet(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Projet démarré avec succès");
            response.put("statut", projet.getStatut());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // PUT terminer un projet
    @PutMapping("/{id}/terminer")
    public ResponseEntity<Map<String, String>> terminerProjet(@PathVariable Long id) {
        try {
            Projet projet = projetService.terminerProjet(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Projet terminé avec succès");
            response.put("statut", projet.getStatut());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}