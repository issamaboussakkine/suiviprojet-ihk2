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
public class LivrableRepositoryTest {

  @Autowired
  private LivrableRepository livrableRepository;

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
    phase.setEtatRealisation(false);
    phase.setEtatFacturation(false);
    phase.setEtatPaiement(false);
    phase.setProjet(projet);
    return phaseRepository.save(phase);
  }

  @Test
  void testSauvegarderLivrable() {
    Phase phase = createPhase("PH-L1");

    Livrable livrable = new Livrable();
    livrable.setCode("LIV001");
    livrable.setLibelle("Cahier des charges");
    livrable.setDescription("Document de spécifications");
    livrable.setChemin("/docs/cdc.pdf");
    livrable.setPhase(phase);

    Livrable saved = livrableRepository.save(livrable);

    assertNotNull(saved.getId());
    assertEquals("Cahier des charges", saved.getLibelle());
  }

  @Test
  void testTrouverLivrablesByPhase() {
    Phase phase = createPhase("PH-L2");

    Livrable l1 = new Livrable();
    l1.setCode("LIV002");
    l1.setLibelle("Specs techniques");
    l1.setPhase(phase);
    livrableRepository.save(l1);

    Livrable l2 = new Livrable();
    l2.setCode("LIV003");
    l2.setLibelle("Architecture");
    l2.setPhase(phase);
    livrableRepository.save(l2);

    List<Livrable> result = livrableRepository.findByPhaseId(phase.getId());

    assertEquals(2, result.size());
  }

  @Test
  void testSupprimerLivrable() {
    Phase phase = createPhase("PH-L3");

    Livrable livrable = new Livrable();
    livrable.setCode("LIV004");
    livrable.setLibelle("Manuel utilisateur");
    livrable.setPhase(phase);
    Livrable saved = livrableRepository.save(livrable);

    livrableRepository.deleteById(saved.getId());

    assertFalse(livrableRepository.findById(saved.getId()).isPresent());
  }
}