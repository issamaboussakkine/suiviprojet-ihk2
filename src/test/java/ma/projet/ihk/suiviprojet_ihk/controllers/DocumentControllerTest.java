package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import ma.projet.ihk.suiviprojet_ihk.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires pour DocumentController
 * Teste les endpoints REST de gestion des documents
 */
@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.DocumentController.class)
public class DocumentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DocumentService documentService;

  @Test
  void testGetAllDocuments() throws Exception {
    when(documentService.getAllDocuments())
            .thenReturn(Arrays.asList(new Document(), new Document()));

    mockMvc.perform(get("/api/documents"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetDocumentById_Trouve() throws Exception {
    Document doc = new Document();
    doc.setId(1);
    doc.setLibelle("CDC");

    when(documentService.getDocumentById(1L))
            .thenReturn(Optional.of(doc));

    mockMvc.perform(get("/api/documents/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetDocumentById_NonTrouve() throws Exception {
    when(documentService.getDocumentById(99L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/documents/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testCreateDocument() throws Exception {
    Document doc = new Document();
    doc.setLibelle("Architecture");

    when(documentService.saveDocument(any(Document.class)))
            .thenReturn(doc);

    mockMvc.perform(post("/api/documents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"Architecture\",\"code\":\"DOC001\"}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testUpdateDocument_Trouve() throws Exception {
    Document doc = new Document();
    doc.setLibelle("Architecture modifié");

    when(documentService.updateDocument(eq(1L), any(Document.class)))
            .thenReturn(doc);

    mockMvc.perform(put("/api/documents/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"Architecture modifié\"}"))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdateDocument_NonTrouve() throws Exception {
    when(documentService.updateDocument(eq(99L), any(Document.class)))
            .thenReturn(null);

    mockMvc.perform(put("/api/documents/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"test\"}"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteDocument() throws Exception {
    doNothing().when(documentService).deleteDocument(1L);

    mockMvc.perform(delete("/api/documents/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetDocumentsByProjet() throws Exception {
    when(documentService.getDocumentsByProjet(1L))
            .thenReturn(Arrays.asList(new Document()));

    mockMvc.perform(get("/api/documents/projet/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testCountDocumentsByProjet() throws Exception {
    when(documentService.countDocumentsByProjet(1L)).thenReturn(3L);

    mockMvc.perform(get("/api/documents/projet/1/count"))
            .andExpect(status().isOk())
            .andExpect(content().string("3"));
  }

  @Test
  void testHasDocuments() throws Exception {
    when(documentService.hasDocuments(1L)).thenReturn(true);

    mockMvc.perform(get("/api/documents/projet/1/exists"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
  }
}