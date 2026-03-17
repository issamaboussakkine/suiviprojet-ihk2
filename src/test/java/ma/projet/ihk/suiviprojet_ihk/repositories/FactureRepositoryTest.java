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
public class FactureRepositoryTest {

  @Autowired
  private FactureRepository factureRepository;

  @Autowired
  private PhaseRepository phaseRepository;

  @Autowired
  private ProjetRepository projetRepository;

  @Autowired
  private OrganismeRepository organismeRepository;

  private Phase createPhase(String code) {
    Organisme org = new Organisme();
    org.setCode("ORG-" + code);
    org.setNom("Organisme " + code);
    organismeRepository.save(org);

    Projet projet = new Projet();
    projet.setCode("PRJ-" + code);
    projet.setNom("Projet " + code);
    projet.setDateDebut(LocalDate.now());
    projet.setDateFin(LocalDate.now().plusMonths(6));
    projet.setMontant(100000.0);
    projet.setOrganisme(org);
    projetRepository.save(projet);

    Phase phase = new Phase();
    phase.setCode(code);
    phase.setLibelle("Phase " + code);
    phase.setDateDebut(LocalDate.now());
    phase.setDateFin(LocalDate.now().plusMonths(1));
    phase.setMontant(20000.0);
    phase.setEtatRealisation(true);
    phase.setEtatFacturation(false);
    phase.setEtatPaiement(false);
    phase.setProjet(projet);
    return phaseRepository.save(phase);
  }

  @Test
  void testSauvegarderFacture() {
    Phase phase = createPhase("PH-F1");

    Facture facture = new Facture();
    facture.setDateFacture(LocalDate.now());
    facture.setPhase(phase);

    Facture saved = factureRepository.save(facture);

    assertNotNull(saved.getId());
    assertNotNull(saved.getDateFacture());
  }

  @Test
  void testTrouverFactureParPhase() {
    Phase phase = createPhase("PH-F2");

    Facture facture = new Facture();
    facture.setDateFacture(LocalDate.now());
    facture.setPhase(phase);
    factureRepository.save(facture);

    Facture found = factureRepository.findByPhaseId(phase.getId());

    assertNotNull(found);
    assertEquals(phase.getId(), found.getPhase().getId());
  }

  @Test
  void testRechercherFacturesParPeriode() {
    Phase phase = createPhase("PH-F3");

    Facture facture = new Facture();
    facture.setDateFacture(LocalDate.now());
    facture.setPhase(phase);
    factureRepository.save(facture);

    List<Facture> result = factureRepository.findByDateFactureBetween(
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1)
    );

    assertFalse(result.isEmpty());
  }

  @Test
  void testSupprimerFacture() {
    Phase phase = createPhase("PH-F4");

    Facture facture = new Facture();
    facture.setDateFacture(LocalDate.now());
    facture.setPhase(phase);
    Facture saved = factureRepository.save(facture);

    factureRepository.deleteById(saved.getId());

    assertFalse(factureRepository.findById(saved.getId()).isPresent());
  }
}