package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.service.ProjetService;
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

@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.ProjetController.class)
public class ProjetControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProjetService projetService;

  @Test
  void testGetAllProjets() throws Exception {
    when(projetService.getAllProjets())
            .thenReturn(Arrays.asList(new Projet(), new Projet()));

    mockMvc.perform(get("/api/projets"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetProjetById_Trouve() throws Exception {
    Projet projet = new Projet();
    projet.setId(1);

    when(projetService.getProjetById(1L))
            .thenReturn(Optional.of(projet));

    mockMvc.perform(get("/api/projets/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetProjetById_NonTrouve() throws Exception {
    when(projetService.getProjetById(99L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/projets/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testGetProjetByCode_Trouve() throws Exception {
    Projet projet = new Projet();
    projet.setCode("PRJ001");

    when(projetService.getProjetByCode("PRJ001"))
            .thenReturn(Optional.of(projet));

    mockMvc.perform(get("/api/projets/code/PRJ001"))
            .andExpect(status().isOk());
  }

  @Test
  void testCreateProjet() throws Exception {
    Projet projet = new Projet();
    projet.setNom("Système de suivi");

    when(projetService.saveProjet(any(Projet.class)))
            .thenReturn(projet);

    mockMvc.perform(post("/api/projets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"Système de suivi\",\"code\":\"PRJ001\",\"montant\":150000}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testUpdateProjet_Trouve() throws Exception {
    Projet projet = new Projet();
    projet.setNom("Projet modifié");

    when(projetService.updateProjet(eq(1L), any(Projet.class)))
            .thenReturn(projet);

    mockMvc.perform(put("/api/projets/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"Projet modifié\"}"))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdateProjet_NonTrouve() throws Exception {
    when(projetService.updateProjet(eq(99L), any(Projet.class)))
            .thenReturn(null);

    mockMvc.perform(put("/api/projets/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"test\"}"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteProjet() throws Exception {
    doNothing().when(projetService).deleteProjet(1L);

    mockMvc.perform(delete("/api/projets/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetProjetsByOrganisme() throws Exception {
    when(projetService.getProjetsByOrganisme(1L))
            .thenReturn(Arrays.asList(new Projet()));

    mockMvc.perform(get("/api/projets/organisme/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetProjetsByChefProjet() throws Exception {
    when(projetService.getProjetsByChefProjet(1L))
            .thenReturn(Arrays.asList(new Projet()));

    mockMvc.perform(get("/api/projets/chef/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testSearchProjetsByNom() throws Exception {
    when(projetService.searchProjetsByNom("suivi"))
            .thenReturn(Arrays.asList(new Projet()));

    mockMvc.perform(get("/api/projets/search")
                    .param("nom", "suivi"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetMontantTotal() throws Exception {
    when(projetService.getMontantTotalProjets()).thenReturn(500000.0);

    mockMvc.perform(get("/api/projets/montant-total"))
            .andExpect(status().isOk())
            .andExpect(content().string("500000.0"));
  }

  @Test
  void testCountProjetsEnCours() throws Exception {
    when(projetService.countProjetsEnCours()).thenReturn(3L);

    mockMvc.perform(get("/api/projets/count/en-cours"))
            .andExpect(status().isOk())
            .andExpect(content().string("3"));
  }

  @Test
  void testCountProjetsTermines() throws Exception {
    when(projetService.countProjetsTermines()).thenReturn(5L);

    mockMvc.perform(get("/api/projets/count/termines"))
            .andExpect(status().isOk())
            .andExpect(content().string("5"));
  }
}