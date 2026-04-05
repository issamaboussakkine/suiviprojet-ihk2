package ma.projet.ihk.suiviprojet_ihk.repositories;

import ma.projet.ihk.suiviprojet_ihk.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PhaseRepositoryTest {

  @Autowired
  private PhaseRepository phaseRepository;

  @Autowired
  private ProjetRepository projetRepository;

  @Autowired
  private OrganismeRepository organismeRepository;

  private Projet createProjet(String code) {
    Organisme org = new Organisme();
    org.setCode("ORG-" + code);
    org.setNom("Organisme " + code);
    organismeRepository.save(org);

    Projet projet = new Projet();
    projet.setCode(code);
    projet.setNom("Projet " + code);
    projet.setDateDebut(LocalDate.now());
    projet.setDateFin(LocalDate.now().plusMonths(6));
    projet.setMontant(100000.0);
    projet.setOrganisme(org);
    return projetRepository.save(projet);
  }

  @Test
  void testSauvegarderPhase() {
    Projet projet = createProjet("PRJ-P1");

    Phase phase = new Phase();
    phase.setCode("PH001");
    phase.setLibelle("Analyse");
    phase.setDateDebut(LocalDate.now());
    phase.setDateFin(LocalDate.now().plusMonths(1));
    phase.setMontant(20000.0);
    phase.setEtatRealisation(false);
    phase.setEtatFacturation(false);
    phase.setEtatPaiement(false);
    phase.setProjet(projet);

    Phase saved = phaseRepository.save(phase);

    assertNotNull(saved.getId());
    assertEquals("Analyse", saved.getLibelle());
  }

  @Test
  void testPhasesTermineesNonFacturees() {
    Projet projet = createProjet("PRJ-P2");

    Phase phase = new Phase();
    phase.setCode("PH002");
    phase.setLibelle("Conception");
    phase.setDateDebut(LocalDate.now());
    phase.setDateFin(LocalDate.now().plusMonths(1));
    phase.setMontant(30000.0);
    phase.setEtatRealisation(true);   // terminée
    phase.setEtatFacturation(false);  // non facturée
    phase.setEtatPaiement(false);
    phase.setProjet(projet);
    phaseRepository.save(phase);

    List<Phase> result = phaseRepository
            .findByEtatRealisationTrueAndEtatFacturationFalse();

    assertFalse(result.isEmpty());
  }

  @Test
  void testPhasesFactureesNonPayees() {
    Projet projet = createProjet("PRJ-P3");

    Phase phase = new Phase();
    phase.setCode("PH003");
    phase.setLibelle("Développement");
    phase.setDateDebut(LocalDate.now());
    phase.setDateFin(LocalDate.now().plusMonths(2));
    phase.setMontant(50000.0);
    phase.setEtatRealisation(true);
    phase.setEtatFacturation(true);   // facturée
    phase.setEtatPaiement(false);     // non payée
    phase.setProjet(projet);
    phaseRepository.save(phase);

    List<Phase> result = phaseRepository
            .findByEtatFacturationTrueAndEtatPaiementFalse();

    assertFalse(result.isEmpty());
  }
}