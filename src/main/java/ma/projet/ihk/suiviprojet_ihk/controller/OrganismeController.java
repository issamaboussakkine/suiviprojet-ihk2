package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.service.OrganismeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/organismes")
@CrossOrigin(origins = "*")
public class OrganismeController {

    @Autowired
    private OrganismeService organismeService;

    // Récupérer tous les organismes
    @GetMapping
    public List<Organisme> getAllOrganismes() {
        return organismeService.getAllOrganismes();
    }

    // Récupérer un organisme par ID
    @GetMapping("/{id}")
    public ResponseEntity<Organisme> getOrganismeById(@PathVariable Long id) {
        return organismeService.getOrganismeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer un organisme par code
    @GetMapping("/code/{code}")
    public ResponseEntity<Organisme> getOrganismeByCode(@PathVariable String code) {
        return organismeService.getOrganismeByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouvel organisme
    @PostMapping
    public ResponseEntity<Organisme> createOrganisme(@RequestBody Organisme organisme) {
        Organisme saved = organismeService.saveOrganisme(organisme);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Modifier un organisme
    @PutMapping("/{id}")
    public ResponseEntity<Organisme> updateOrganisme(@PathVariable Long id, @RequestBody Organisme organisme) {
        Organisme updated = organismeService.updateOrganisme(id, organisme);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Supprimer un organisme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisme(@PathVariable Long id) {
        organismeService.deleteOrganisme(id);
        return ResponseEntity.noContent().build();
    }

    // Rechercher des organismes par nom
    @GetMapping("/search")
    public List<Organisme> searchOrganismesByNom(@RequestParam String nom) {
        return organismeService.searchOrganismesByNom(nom);
    }

    // Récupérer tous les projets d'un organisme
    @GetMapping("/{id}/projets")
    public ResponseEntity<List<Projet>> getProjetsByOrganisme(@PathVariable Long id) {
        List<Projet> projets = organismeService.getProjetsByOrganisme(id);
        return ResponseEntity.ok(projets);
    }

    // Compter le nombre de projets d'un organisme
    @GetMapping("/{id}/projets/count")
    public ResponseEntity<Long> countProjetsByOrganisme(@PathVariable Long id) {
        return ResponseEntity.ok(organismeService.countProjetsByOrganisme(id));
    }

    // Vérifier si un organisme a des projets en cours
    @GetMapping("/{id}/projets/en-cours")
    public ResponseEntity<Boolean> hasProjetsEnCours(@PathVariable Long id) {
        return ResponseEntity.ok(organismeService.hasProjetsEnCours(id));
    }
}