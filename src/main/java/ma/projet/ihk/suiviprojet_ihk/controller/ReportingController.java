package ma.projet.ihk.suiviprojet_ihk.controller;

import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reporting")
@CrossOrigin(origins = "*")
public class ReportingController {

  @Autowired
  private ReportingService reportingService;

  /** Phases terminées mais non facturées */
  @GetMapping("/phases/terminees-non-facturees")
  public ResponseEntity<List<Phase>> getPhasesTermineesNonFacturees() {
    return ResponseEntity.ok(reportingService.getPhasesTermineesNonFacturees());
  }

  /** Phases facturées mais non payées */
  @GetMapping("/phases/facturees-non-payees")
  public ResponseEntity<List<Phase>> getPhasesFactureesNonPayees() {
    return ResponseEntity.ok(reportingService.getPhasesFactureesNonPayees());
  }

  /** Phases payées */
  @GetMapping("/phases/payees")
  public ResponseEntity<List<Phase>> getPhasesPayees() {
    return ResponseEntity.ok(reportingService.getPhasesPayees());
  }

  /** Projets en cours */
  @GetMapping("/projets/en-cours")
  public ResponseEntity<List<Projet>> getProjetsEnCours() {
    return ResponseEntity.ok(reportingService.getProjetsEnCours());
  }

  /** Projets clôturés */
  @GetMapping("/projets/clotures")
  public ResponseEntity<List<Projet>> getProjetsClotures() {
    return ResponseEntity.ok(reportingService.getProjetsClotures());
  }

  /** Tableau de bord général */
  @GetMapping("/tableau-de-bord")
  public ResponseEntity<Map<String, Object>> getTableauDeBord() {
    return ResponseEntity.ok(reportingService.getTableauDeBord());
  }
}