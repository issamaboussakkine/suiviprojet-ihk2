package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.repositories.FactureRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingService {

  @Autowired
  private PhaseRepository phaseRepository;

  @Autowired
  private ProjetRepository projetRepository;

  @Autowired
  private FactureRepository factureRepository;

  /** Phases terminées mais non facturées */
  public List<Phase> getPhasesTermineesNonFacturees() {
    return phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse();
  }

  /** Phases facturées mais non payées */
  public List<Phase> getPhasesFactureesNonPayees() {
    return phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse();
  }

  /** Phases payées */
  public List<Phase> getPhasesPayees() {
    return phaseRepository.findByEtatPaiementTrue();
  }

  /** Projets en cours */
  public List<Projet> getProjetsEnCours() {
    LocalDate aujourdhui = LocalDate.now();
    return projetRepository.findAll()
            .stream()
            .filter(p -> p.getDateFin() != null
                    && !p.getDateFin().isBefore(aujourdhui))
            .collect(java.util.stream.Collectors.toList());
  }

  /** Projets clôturés */
  public List<Projet> getProjetsClotures() {
    LocalDate aujourdhui = LocalDate.now();
    return projetRepository.findAll()
            .stream()
            .filter(p -> p.getDateFin() != null
                    && p.getDateFin().isBefore(aujourdhui))
            .collect(java.util.stream.Collectors.toList());
  }

  /** Tableau de bord général */
  public Map<String, Object> getTableauDeBord() {
    LocalDate aujourdhui = LocalDate.now();
    Map<String, Object> dashboard = new HashMap<>();

    dashboard.put("totalProjets", projetRepository.count());
    dashboard.put("projetsEnCours", getProjetsEnCours().size());
    dashboard.put("projetsClotures", getProjetsClotures().size());
    dashboard.put("phasesTermineesNonFacturees",
            getPhasesTermineesNonFacturees().size());
    dashboard.put("phasesFactureesNonPayees",
            getPhasesFactureesNonPayees().size());
    dashboard.put("phasesPayees",
            getPhasesPayees().size());
    dashboard.put("totalFactures", factureRepository.count());

    return dashboard;
  }
}