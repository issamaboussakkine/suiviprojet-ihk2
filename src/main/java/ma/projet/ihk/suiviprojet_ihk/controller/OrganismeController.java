package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.dto.OrganismeDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.exception.OrganismeNotFoundException;
import ma.projet.ihk.suiviprojet_ihk.mapper.OrganismeMapper;
import ma.projet.ihk.suiviprojet_ihk.service.OrganismeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organismes")
@CrossOrigin(origins = "*")
public class OrganismeController {

    @Autowired
    private OrganismeService organismeService;

    @Autowired
    private OrganismeMapper organismeMapper;

    // GET tous les organismes
    @GetMapping
    public List<OrganismeDTO> getAllOrganismes() {
        List<Organisme> organismes = organismeService.getAllOrganismes();
        return organismes.stream()
                .map(organismeMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<OrganismeDTO> getOrganismeById(@PathVariable Long id) {
        Organisme organisme = organismeService.getOrganismeById(id)
                .orElseThrow(() -> new OrganismeNotFoundException("Organisme non trouvé avec l'ID: " + id));
        return ResponseEntity.ok(organismeMapper.toDto(organisme));
    }

    // GET par code
    @GetMapping("/code/{code}")
    public ResponseEntity<OrganismeDTO> getOrganismeByCode(@PathVariable String code) {
        Organisme organisme = organismeService.getOrganismeByCode(code)
                .orElseThrow(() -> new RuntimeException("Organisme non trouvé avec code: " + code));
        return ResponseEntity.ok(organismeMapper.toDto(organisme));
    }

    // POST créer un organisme
    @PostMapping
    public ResponseEntity<OrganismeDTO> createOrganisme(@RequestBody OrganismeDTO dto) {
        Organisme organisme = organismeMapper.toEntity(dto);
        Organisme saved = organismeService.saveOrganisme(organisme);
        return new ResponseEntity<>(organismeMapper.toDto(saved), HttpStatus.CREATED);
    }

    // PUT modifier un organisme
    @PutMapping("/{id}")
    public ResponseEntity<OrganismeDTO> updateOrganisme(@PathVariable Long id, @RequestBody OrganismeDTO dto) {
        Organisme existingOrganisme = organismeService.getOrganismeById(id)
                .orElseThrow(() -> new OrganismeNotFoundException("Organisme non trouvé avec l'ID: " + id));

        organismeMapper.updateEntityFromDto(dto, existingOrganisme);
        Organisme updated = organismeService.saveOrganisme(existingOrganisme);
        return ResponseEntity.ok(organismeMapper.toDto(updated));
    }

    // DELETE supprimer un organisme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisme(@PathVariable Long id) {
        organismeService.deleteOrganisme(id);
        return ResponseEntity.noContent().build();
    }

    // GET recherche par nom
    @GetMapping("/search")
    public List<OrganismeDTO> searchOrganismesByNom(@RequestParam String nom) {
        List<Organisme> organismes = organismeService.searchOrganismesByNom(nom);
        return organismes.stream()
                .map(organismeMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET tous les projets d'un organisme
    @GetMapping("/{id}/projets")
    public ResponseEntity<List<Projet>> getProjetsByOrganisme(@PathVariable Long id) {
        List<Projet> projets = organismeService.getProjetsByOrganisme(id);
        return ResponseEntity.ok(projets);
    }

    // GET count projets d'un organisme
    @GetMapping("/{id}/projets/count")
    public ResponseEntity<Long> countProjetsByOrganisme(@PathVariable Long id) {
        return ResponseEntity.ok(organismeService.countProjetsByOrganisme(id));
    }

    // GET vérifier si un organisme a des projets en cours
    @GetMapping("/{id}/projets/en-cours")
    public ResponseEntity<Boolean> hasProjetsEnCours(@PathVariable Long id) {
        return ResponseEntity.ok(organismeService.hasProjetsEnCours(id));
    }
}