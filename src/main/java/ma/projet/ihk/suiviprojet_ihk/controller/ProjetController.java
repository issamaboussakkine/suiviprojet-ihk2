package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "*")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    // Récupérer tous les projets
    @GetMapping
    public List<Projet> getAllProjets() {
        return projetService.getAllProjets();
    }

    // Récupérer un projet par ID
    @GetMapping("/{id}")
    public ResponseEntity<Projet> getProjetById(@PathVariable Long id) {
        return projetService.getProjetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer un projet par code
    @GetMapping("/code/{code}")
    public ResponseEntity<Projet> getProjetByCode(@PathVariable String code) {
        return projetService.getProjetByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouveau projet
    @PostMapping
    public ResponseEntity<Projet> createProjet(@RequestBody Projet projet) {
        Projet saved = projetService.saveProjet(projet);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Modifier un projet
    @PutMapping("/{id}")
    public ResponseEntity<Projet> updateProjet(@PathVariable Long id, @RequestBody Projet projet) {
        Projet updated = projetService.updateProjet(id, projet);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Supprimer un projet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        projetService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }

    // Rechercher les projets par organisme
    @GetMapping("/organisme/{organismeId}")
    public List<Projet> getProjetsByOrganisme(@PathVariable Long organismeId) {
        return projetService.getProjetsByOrganisme(organismeId);
    }

    // Rechercher les projets par chef de projet
    @GetMapping("/chef/{chefProjetId}")
    public List<Projet> getProjetsByChefProjet(@PathVariable Long chefProjetId) {
        return projetService.getProjetsByChefProjet(chefProjetId);
    }

    // Rechercher des projets par nom (mots-clés)
    @GetMapping("/search")
    public List<Projet> searchProjetsByNom(@RequestParam String nom) {
        return projetService.searchProjetsByNom(nom);
    }

    // Obtenir le montant total de tous les projets
    @GetMapping("/montant-total")
    public ResponseEntity<Double> getMontantTotal() {
        return ResponseEntity.ok(projetService.getMontantTotalProjets());
    }

    // Compter les projets en cours
    @GetMapping("/count/en-cours")
    public ResponseEntity<Long> countProjetsEnCours() {
        return ResponseEntity.ok(projetService.countProjetsEnCours());
    }

    // Compter les projets terminés
    @GetMapping("/count/termines")
    public ResponseEntity<Long> countProjetsTermines() {
        return ResponseEntity.ok(projetService.countProjetsTermines());
    }
}