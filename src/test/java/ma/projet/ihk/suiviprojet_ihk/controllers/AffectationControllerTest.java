package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import ma.projet.ihk.suiviprojet_ihk.service.AffectationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires pour AffectationController
 * @WebMvcTest :  teste uniquement la couche HTTP sans démarrer le serveur complet
 * MockMvc :  simule les requêtes HTTP
 */
@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.AffectationController.class)
public class AffectationControllerTest {

  @Autowired
  private MockMvc mockMvc;


  @MockitoBean
  private AffectationService affectationService ;

  @Test
  void testGetAllAffectations() throws Exception {
    Affectation a1 = new Affectation();
    Affectation a2 = new Affectation();

    when(affectationService.getAllAffectations())
            .thenReturn(Arrays.asList(a1, a2));

    mockMvc.perform(get("/api/affectations"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetAffectationById_Trouve() throws Exception {
    Affectation affectation = new Affectation();
    AffectationId id = new AffectationId(1, 1);
    affectation.setId(id);

    when(affectationService.getAffectationById(any(AffectationId.class)))
            .thenReturn(Optional.of(affectation));

    mockMvc.perform(get("/api/affectations/1/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetAffectationById_NonTrouve() throws Exception {
    when(affectationService.getAffectationById(any(AffectationId.class)))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/affectations/99/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testCreateAffectation() throws Exception {
    Affectation affectation = new Affectation();

    when(affectationService.saveAffectation(any(Affectation.class)))
            .thenReturn(affectation);

    mockMvc.perform(post("/api/affectations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dateDebut\":\"2026-01-01\",\"dateFin\":\"2026-06-01\"}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testDeleteAffectation() throws Exception {
    doNothing().when(affectationService)
            .deleteAffectation(any(AffectationId.class));

    mockMvc.perform(delete("/api/affectations/1/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetAffectationsByPhase() throws Exception {
    when(affectationService.getAffectationsByPhase(1L))
            .thenReturn(Arrays.asList(new Affectation()));

    mockMvc.perform(get("/api/affectations/phase/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetAffectationsByEmploye() throws Exception {
    when(affectationService.getAffectationsByEmploye(1L))
            .thenReturn(Arrays.asList(new Affectation()));

    mockMvc.perform(get("/api/affectations/employe/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testIsEmployeDisponible() throws Exception {
    when(affectationService.isEmployeDisponible(any(), any(), any()))
            .thenReturn(true);

    mockMvc.perform(get("/api/affectations/disponible/1")
                    .param("dateDebut", "2026-01-01")
                    .param("dateFin", "2026-06-01"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
  }

  @Test
  void testCountEmployesByPhase() throws Exception {
    when(affectationService.countEmployesByPhase(1L)).thenReturn(3L);

    mockMvc.perform(get("/api/affectations/phase/1/count-employes"))
            .andExpect(status().isOk())
            .andExpect(content().string("3"));
  }

  @Test
  void testCountPhasesByEmploye() throws Exception {
    when(affectationService.countPhasesByEmploye(1L)).thenReturn(2L);

    mockMvc.perform(get("/api/affectations/employe/1/count-phases"))
            .andExpect(status().isOk())
            .andExpect(content().string("2"));
  }
}