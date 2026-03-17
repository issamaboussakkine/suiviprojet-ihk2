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
public class AffectationRepositoryTest {

  @Autowired
  private AffectationRepository affectationRepository;

  @Autowired
  private EmployeRepository employeRepository;

  @Autowired
  private PhaseRepository phaseRepository;

  @Autowired
  private ProjetRepository projetRepository;

  @Autowired
  private OrganismeRepository organismeRepository;

  @Autowired
  private ProfilRepository profilRepository;

  private Employe createEmploye(String matricule) {
    Profil profil = new Profil();
    profil.setCode("P-" + matricule);
    profil.setLibelle("Profil " + matricule);
    profilRepository.save(profil);

    Employe emp = new Employe();
    emp.setMatricule(matricule);
    emp.setNom("Nom " + matricule);
    emp.setLogin("login" + matricule);
    emp.setPassword("pass");
    emp.setProfil(profil);
    return employeRepository.save(emp);
  }

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
  void testAffecterEmployeAPhase() {
    Employe emp = createEmploye("EMP-A1");
    Phase phase = createPhase("PH-A1");

    AffectationId id = new AffectationId(emp.getId(), phase.getId());
    Affectation affectation = new Affectation();
    affectation.setId(id);
    affectation.setEmploye(emp);
    affectation.setPhase(phase);
    affectation.setDateDebut(LocalDate.now());
    affectation.setDateFin(LocalDate.now().plusMonths(1));

    Affectation saved = affectationRepository.save(affectation);

    assertNotNull(saved.getId());
  }

  @Test
  void testTrouverAffectationsParPhase() {
    Employe emp = createEmploye("EMP-A2");
    Phase phase = createPhase("PH-A2");

    AffectationId id = new AffectationId(emp.getId(), phase.getId());
    Affectation affectation = new Affectation();
    affectation.setId(id);
    affectation.setEmploye(emp);
    affectation.setPhase(phase);
    affectation.setDateDebut(LocalDate.now());
    affectation.setDateFin(LocalDate.now().plusMonths(1));
    affectationRepository.save(affectation);

    List<Affectation> result = affectationRepository
            .findByPhaseId(phase.getId());

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
  }

  @Test
  void testTrouverAffectationsParEmploye() {
    Employe emp = createEmploye("EMP-A3");
    Phase phase = createPhase("PH-A3");

    AffectationId id = new AffectationId(emp.getId(), phase.getId());
    Affectation affectation = new Affectation();
    affectation.setId(id);
    affectation.setEmploye(emp);
    affectation.setPhase(phase);
    affectation.setDateDebut(LocalDate.now());
    affectation.setDateFin(LocalDate.now().plusMonths(1));
    affectationRepository.save(affectation);

    List<Affectation> result = affectationRepository
            .findByEmployeId(emp.getId());

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
  }
}