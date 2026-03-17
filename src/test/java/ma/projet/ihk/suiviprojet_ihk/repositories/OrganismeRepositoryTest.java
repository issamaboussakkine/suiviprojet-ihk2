package ma.projet.ihk.suiviprojet_ihk.repositories;


import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrganismeRepositoryTest {

  @Autowired
  private OrganismeRepository organismeRepository;

  @Test
  void testSauvegarderOrganisme() {
    Organisme org = new Organisme();
    org.setCode("ORG001");
    org.setNom("OFPPT");

    Organisme saved = organismeRepository.save(org);

    assertNotNull(saved.getId());
    assertEquals("OFPPT", saved.getNom());
  }

  @Test
  void testTrouverParCode() {
    Organisme org = new Organisme();
    org.setCode("ORG002");
    org.setNom("CIH Bank");
    organismeRepository.save(org);

    Organisme found = organismeRepository.findByCode("ORG002");

    assertNotNull(found);
    assertEquals("CIH Bank", found.getNom());
  }

  @Test
  void testRechercherParNom() {
    Organisme org = new Organisme();
    org.setCode("ORG003");
    org.setNom("Ministère de l'Education");
    organismeRepository.save(org);

    List<Organisme> results = organismeRepository.findByNomContaining("Education");

    assertFalse(results.isEmpty());
  }
}