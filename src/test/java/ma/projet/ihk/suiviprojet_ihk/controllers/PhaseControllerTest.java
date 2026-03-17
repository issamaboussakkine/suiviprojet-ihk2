package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.service.PhaseService;
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

@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.PhaseController.class)
public class PhaseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PhaseService phaseService;

  @Test
  void testGetAllPhases() throws Exception {
    when(phaseService.getAllPhases())
            .thenReturn(Arrays.asList(new Phase(), new Phase()));

    mockMvc.perform(get("/api/phases"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetPhaseById_Trouve() throws Exception {
    Phase phase = new Phase();
    phase.setId(1);

    when(phaseService.getPhaseById(1L))
            .thenReturn(Optional.of(phase));

    mockMvc.perform(get("/api/phases/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetPhaseById_NonTrouve() throws Exception {
    when(phaseService.getPhaseById(99L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/phases/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testCreatePhase() throws Exception {
    Phase phase = new Phase();
    phase.setLibelle("Analyse");

    when(phaseService.savePhase(any(Phase.class)))
            .thenReturn(phase);

    mockMvc.perform(post("/api/phases")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"Analyse\",\"code\":\"PH001\",\"montant\":20000}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testUpdatePhase_Trouve() throws Exception {
    Phase phase = new Phase();
    phase.setLibelle("Analyse modifié");

    when(phaseService.updatePhase(eq(1L), any(Phase.class)))
            .thenReturn(phase);

    mockMvc.perform(put("/api/phases/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"Analyse modifié\"}"))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdatePhase_NonTrouve() throws Exception {
    when(phaseService.updatePhase(eq(99L), any(Phase.class)))
            .thenReturn(null);

    mockMvc.perform(put("/api/phases/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"test\"}"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testDeletePhase() throws Exception {
    doNothing().when(phaseService).deletePhase(1L);

    mockMvc.perform(delete("/api/phases/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetPhasesByProjet() throws Exception {
    when(phaseService.getPhasesByProjet(1L))
            .thenReturn(Arrays.asList(new Phase()));

    mockMvc.perform(get("/api/phases/projet/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetPhasesTermineesNonFacturees() throws Exception {
    when(phaseService.getPhasesTermineesNonFacturees())
            .thenReturn(Arrays.asList(new Phase()));

    mockMvc.perform(get("/api/phases/statut/terminees-non-facturees"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetPhasesFactureesNonPayees() throws Exception {
    when(phaseService.getPhasesFactureesNonPayees())
            .thenReturn(Arrays.asList(new Phase()));

    mockMvc.perform(get("/api/phases/statut/facturees-non-payees"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetPhasesPayees() throws Exception {
    when(phaseService.getPhasesPayees())
            .thenReturn(Arrays.asList(new Phase()));

    mockMvc.perform(get("/api/phases/statut/payees"))
            .andExpect(status().isOk());
  }

  @Test
  void testMarquerTerminee() throws Exception {
    Phase phase = new Phase();
    phase.setEtatRealisation(true);

    when(phaseService.marquerTerminee(1L)).thenReturn(phase);

    mockMvc.perform(put("/api/phases/1/terminer"))
            .andExpect(status().isOk());
  }

  @Test
  void testMarquerFacturee() throws Exception {
    Phase phase = new Phase();
    phase.setEtatFacturation(true);

    when(phaseService.marquerFacturee(1L)).thenReturn(phase);

    mockMvc.perform(put("/api/phases/1/facturer"))
            .andExpect(status().isOk());
  }

  @Test
  void testMarquerPayee() throws Exception {
    Phase phase = new Phase();
    phase.setEtatPaiement(true);

    when(phaseService.marquerPayee(1L)).thenReturn(phase);

    mockMvc.perform(put("/api/phases/1/payer"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetMontantTotalByProjet() throws Exception {
    when(phaseService.getMontantTotalPhasesByProjet(1L)).thenReturn(50000.0);

    mockMvc.perform(get("/api/phases/projet/1/montant-total"))
            .andExpect(status().isOk())
            .andExpect(content().string("50000.0"));
  }

  @Test
  void testCountPhasesEnCoursByProjet() throws Exception {
    when(phaseService.countPhasesEnCoursByProjet(1L)).thenReturn(2L);

    mockMvc.perform(get("/api/phases/projet/1/en-cours"))
            .andExpect(status().isOk())
            .andExpect(content().string("2"));
  }
}