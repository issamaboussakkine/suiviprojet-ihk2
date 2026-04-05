package ma.projet.ihk.suiviprojet_ihk.repositories;

import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeRepositoryTest {

  @Autowired
  private EmployeRepository employeRepository;

  @Autowired
  private ProfilRepository profilRepository;

  @Test
  void testSauvegarderEmploye() {
    Profil profil = new Profil();
    profil.setCode("CP");
    profil.setLibelle("Chef de projet");
    profilRepository.save(profil);

    Employe emp = new Employe();
    emp.setMatricule("EMP001");
    emp.setNom("Alami");
    emp.setPrenom("Samir");
    emp.setLogin("alami");
    emp.setPassword("azerty");
    emp.setProfil(profil);

    Employe saved = employeRepository.save(emp);

    assertNotNull(saved.getId());
    assertEquals("Alami", saved.getNom());
  }

  @Test
  void testTrouverParMatricule() {
    Profil profil = new Profil();
    profil.setCode("SEC");
    profil.setLibelle("Secrétaire");
    profilRepository.save(profil);

    Employe emp = new Employe();
    emp.setMatricule("EMP002");
    emp.setNom("Benali");
    emp.setLogin("benali");
    emp.setPassword("1234");
    emp.setProfil(profil);
    employeRepository.save(emp);

    Employe found = employeRepository.findByMatricule("EMP002");

    assertNotNull(found);
    assertEquals("Benali", found.getNom());
  }

  @Test
  void testTrouverParLogin() {
    Profil profil = new Profil();
    profil.setCode("COMPT");
    profil.setLibelle("Comptable");
    profilRepository.save(profil);

    Employe emp = new Employe();
    emp.setMatricule("EMP003");
    emp.setNom("Idrissi");
    emp.setLogin("idrissi");
    emp.setPassword("pass123");
    emp.setProfil(profil);
    employeRepository.save(emp);

    Employe found = employeRepository.findByLogin("idrissi");

    assertNotNull(found);
    assertEquals("Idrissi", found.getNom());
  }
}