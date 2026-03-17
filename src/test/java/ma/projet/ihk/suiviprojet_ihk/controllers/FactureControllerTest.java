package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import ma.projet.ihk.suiviprojet_ihk.service.FactureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.FactureController.class)
public class FactureControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FactureService factureService;

  @Test
  void testGetAllFactures() throws Exception {
    when(factureService.getAllFactures())
            .thenReturn(Arrays.asList(new Facture(), new Facture()));

    mockMvc.perform(get("/api/factures"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetFactureById_Trouve() throws Exception {
    Facture facture = new Facture();
    facture.setId(1);

    when(factureService.getFactureById(1L))
            .thenReturn(Optional.of(facture));

    mockMvc.perform(get("/api/factures/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetFactureById_NonTrouve() throws Exception {
    when(factureService.getFactureById(99L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/factures/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testCreateFacture() throws Exception {
    Facture facture = new Facture();
    facture.setDateFacture(LocalDate.now());

    when(factureService.saveFacture(any(Facture.class)))
            .thenReturn(facture);

    mockMvc.perform(post("/api/factures")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dateFacture\":\"2026-03-17\"}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testCreateFacture_PhaseDejaFacturee() throws Exception {
    when(factureService.saveFacture(any(Facture.class)))
            .thenThrow(new RuntimeException("Phase déjà facturée"));

    mockMvc.perform(post("/api/factures")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dateFacture\":\"2026-03-17\"}"))
            .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateFacture_Trouve() throws Exception {
    Facture facture = new Facture();
    facture.setDateFacture(LocalDate.now());

    when(factureService.updateFacture(eq(1L), any(Facture.class)))
            .thenReturn(facture);

    mockMvc.perform(put("/api/factures/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dateFacture\":\"2026-03-17\"}"))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdateFacture_NonTrouve() throws Exception {
    when(factureService.updateFacture(eq(99L), any(Facture.class)))
            .thenReturn(null);

    mockMvc.perform(put("/api/factures/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dateFacture\":\"2026-03-17\"}"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteFacture() throws Exception {
    doNothing().when(factureService).deleteFacture(1L);

    mockMvc.perform(delete("/api/factures/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetFactureByPhase_Trouve() throws Exception {
    Facture facture = new Facture();
    when(factureService.getFactureByPhase(1L))
            .thenReturn(Optional.of(facture));

    mockMvc.perform(get("/api/factures/phase/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetFacturesByPeriode() throws Exception {
    when(factureService.getFacturesByPeriode(any(), any()))
            .thenReturn(Arrays.asList(new Facture()));

    mockMvc.perform(get("/api/factures/periode")
                    .param("dateDebut", "2026-01-01")
                    .param("dateFin", "2026-03-31"))
            .andExpect(status().isOk());
  }

  @Test
  void testPhaseDejaFacturee() throws Exception {
    when(factureService.phaseDejaFacturee(1L)).thenReturn(true);

    mockMvc.perform(get("/api/factures/phase/1/existe"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
  }

  @Test
  void testCountFacturesByPeriode() throws Exception {
    when(factureService.countFacturesByPeriode(any(), any())).thenReturn(5L);

    mockMvc.perform(get("/api/factures/periode/count")
                    .param("dateDebut", "2026-01-01")
                    .param("dateFin", "2026-03-31"))
            .andExpect(status().isOk())
            .andExpect(content().string("5"));
  }
}