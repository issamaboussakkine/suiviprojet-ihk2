package ma.projet.ihk.suiviprojet_ihk.repositories;

import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ProfilRepository
 * AutoConfigureTestDatabase → utilise MySQL réelle au lieu de H2
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfilRepositoryTest {

  @Autowired
  private ProfilRepository profilRepository;

  @Test
  void testSauvegarderProfil() {
    Profil profil = new Profil();
    profil.setCode("CP");
    profil.setLibelle("Chef de projet");

    Profil saved = profilRepository.save(profil);

    assertNotNull(saved.getId());
    assertEquals("CP", saved.getCode());
    assertEquals("Chef de projet", saved.getLibelle());
  }

  @Test
  void testTrouverProfilParCode() {
    Profil profil = new Profil();
    profil.setCode("COMPT");
    profil.setLibelle("Comptable");
    profilRepository.save(profil);

    Profil found = profilRepository.findByCode("COMPT");

    assertNotNull(found);
    assertEquals("Comptable", found.getLibelle());
  }

  @Test
  void testSupprimerProfil() {
    Profil profil = new Profil();
    profil.setCode("SEC");
    profil.setLibelle("Secrétaire");
    Profil saved = profilRepository.save(profil);

    profilRepository.deleteById(saved.getId());

    assertFalse(profilRepository.findById(saved.getId()).isPresent());
  }
}