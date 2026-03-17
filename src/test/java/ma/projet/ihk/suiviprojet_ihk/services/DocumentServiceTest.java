package ma.projet.ihk.suiviprojet_ihk.services;

import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import ma.projet.ihk.suiviprojet_ihk.repositories.DocumentRepository;
import ma.projet.ihk.suiviprojet_ihk.service.DocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

  @Mock
  private DocumentRepository documentRepository;

  @InjectMocks
  private DocumentServiceImpl documentService;

  @Test
  void testSaveDocument() {
    Document doc = new Document();
    doc.setCode("DOC001");
    doc.setLibelle("Architecture");

    when(documentRepository.save(doc)).thenReturn(doc);

    Document result = documentService.saveDocument(doc);

    assertNotNull(result);
    assertEquals("Architecture", result.getLibelle());
    verify(documentRepository, times(1)).save(doc);
  }

  @Test
  void testUpdateDocument_Existe() {
    Document doc = new Document();
    doc.setLibelle("Document modifié");

    when(documentRepository.existsById(1)).thenReturn(true);
    when(documentRepository.save(any(Document.class))).thenReturn(doc);

    Document result = documentService.updateDocument(1L, doc);

    assertNotNull(result);
    assertEquals("Document modifié", result.getLibelle());
  }

  @Test
  void testUpdateDocument_NonTrouve() {
    Document doc = new Document();
    when(documentRepository.existsById(99)).thenReturn(false);

    Document result = documentService.updateDocument(99L, doc);

    assertNull(result);
  }

  @Test
  void testGetDocumentById() {
    Document doc = new Document();
    doc.setId(1);
    doc.setLibelle("CDC");

    when(documentRepository.findById(1)).thenReturn(Optional.of(doc));

    Optional<Document> result = documentService.getDocumentById(1L);

    assertTrue(result.isPresent());
    assertEquals("CDC", result.get().getLibelle());
  }

  @Test
  void testGetDocumentsByProjet() {
    Document d1 = new Document();
    d1.setLibelle("Doc1");
    Document d2 = new Document();
    d2.setLibelle("Doc2");

    when(documentRepository.findByProjetId(1)).thenReturn(Arrays.asList(d1, d2));

    List<Document> result = documentService.getDocumentsByProjet(1L);

    assertEquals(2, result.size());
  }

  @Test
  void testDeleteDocument() {
    doNothing().when(documentRepository).deleteById(1);

    documentService.deleteDocument(1L);

    verify(documentRepository, times(1)).deleteById(1);
  }

  @Test
  void testHasDocuments_Vrai() {
    Document doc = new Document();
    when(documentRepository.findByProjetId(1)).thenReturn(Arrays.asList(doc));

    boolean result = documentService.hasDocuments(1L);

    assertTrue(result);
  }

  @Test
  void testHasDocuments_Faux() {
    when(documentRepository.findByProjetId(1)).thenReturn(Arrays.asList());

    boolean result = documentService.hasDocuments(1L);

    assertFalse(result);
  }

  @Test
  void testCountDocumentsByProjet() {
    Document d1 = new Document();
    Document d2 = new Document();

    when(documentRepository.findByProjetId(1)).thenReturn(Arrays.asList(d1, d2));

    long count = documentService.countDocumentsByProjet(1L);

    assertEquals(2, count);
  }
}