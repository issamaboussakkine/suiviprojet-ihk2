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
public class ProjetRepositoryTest {

  @Autowired
  private ProjetRepository projetRepository;

  @Autowired
  private OrganismeRepository organismeRepository;

  @Autowired
  private EmployeRepository employeRepository;

  @Autowired
  private ProfilRepository profilRepository;

  @Test
  void testSauvegarderProjet() {
    Organisme org = new Organisme();
    org.setCode("ORG001");
    org.setNom("OFPPT");
    organismeRepository.save(org);

    Profil profil = new Profil();
    profil.setCode("CP");
    profil.setLibelle("Chef de projet");
    profilRepository.save(profil);

    Employe chef = new Employe();
    chef.setMatricule("EMP001");
    chef.setNom("Alami");
    chef.setLogin("alami");
    chef.setPassword("azerty");
    chef.setProfil(profil);
    employeRepository.save(chef);

    Projet projet = new Projet();
    projet.setCode("PRJ001");
    projet.setNom("Système de suivi");
    projet.setDescription("Application web");
    projet.setDateDebut(LocalDate.now());
    projet.setDateFin(LocalDate.now().plusMonths(6));
    projet.setMontant(150000.0);
    projet.setOrganisme(org);
    projet.setChefProjet(chef);

    Projet saved = projetRepository.save(projet);

    assertNotNull(saved.getId());
    assertEquals("PRJ001", saved.getCode());
  }

  @Test
  void testTrouverParCode() {
    Organisme org = new Organisme();
    org.setCode("ORG002");
    org.setNom("CIH Bank");
    organismeRepository.save(org);

    Projet projet = new Projet();
    projet.setCode("PRJ002");
    projet.setNom("Refonte SI");
    projet.setDateDebut(LocalDate.now());
    projet.setDateFin(LocalDate.now().plusMonths(3));
    projet.setMontant(80000.0);
    projet.setOrganisme(org);
    projetRepository.save(projet);

    Projet found = projetRepository.findByCode("PRJ002");

    assertNotNull(found);
    assertEquals("Refonte SI", found.getNom());
  }

  @Test
  void testRechercherParOrganisme() {
    Organisme org = new Organisme();
    org.setCode("ORG003");
    org.setNom("Ministère");
    organismeRepository.save(org);

    Projet projet = new Projet();
    projet.setCode("PRJ003");
    projet.setNom("Projet Ministère");
    projet.setDateDebut(LocalDate.now());
    projet.setDateFin(LocalDate.now().plusMonths(12));
    projet.setMontant(200000.0);
    projet.setOrganisme(org);
    projetRepository.save(projet);

    List<Projet> projets = projetRepository.findByOrganismeId(org.getId());

    assertFalse(projets.isEmpty());
    assertEquals(1, projets.size());
  }
}