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
public class DocumentRepositoryTest {

  @Autowired
  private DocumentRepository documentRepository;

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
  void testSauvegarderDocument() {
    Projet projet = createProjet("PRJ-D1");

    Document doc = new Document();
    doc.setCode("DOC001");
    doc.setLibelle("Cahier des charges");
    doc.setDescription("Document principal");
    doc.setChemin("/docs/cdc.pdf");
    doc.setProjet(projet);

    Document saved = documentRepository.save(doc);

    assertNotNull(saved.getId());
    assertEquals("Cahier des charges", saved.getLibelle());
  }

  @Test
  void testTrouverDocumentsByProjet() {
    Projet projet = createProjet("PRJ-D2");

    Document d1 = new Document();
    d1.setCode("DOC002");
    d1.setLibelle("Specs");
    d1.setProjet(projet);
    documentRepository.save(d1);

    Document d2 = new Document();
    d2.setCode("DOC003");
    d2.setLibelle("Architecture");
    d2.setProjet(projet);
    documentRepository.save(d2);

    List<Document> result = documentRepository.findByProjetId(projet.getId());

    assertEquals(2, result.size());
  }

  @Test
  void testSupprimerDocument() {
    Projet projet = createProjet("PRJ-D3");

    Document doc = new Document();
    doc.setCode("DOC004");
    doc.setLibelle("Manuel");
    doc.setProjet(projet);
    Document saved = documentRepository.save(doc);

    documentRepository.deleteById(saved.getId());

    assertFalse(documentRepository.findById(saved.getId()).isPresent());
  }
}