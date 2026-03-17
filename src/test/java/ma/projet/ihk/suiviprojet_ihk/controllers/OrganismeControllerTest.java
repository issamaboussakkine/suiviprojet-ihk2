package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.service.OrganismeService;
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

@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.OrganismeController.class)
public class OrganismeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private OrganismeService organismeService;

  @Test
  void testGetAllOrganismes() throws Exception {
    when(organismeService.getAllOrganismes())
            .thenReturn(Arrays.asList(new Organisme(), new Organisme()));

    mockMvc.perform(get("/api/organismes"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetOrganismeById_Trouve() throws Exception {
    Organisme org = new Organisme();
    org.setId(1);
    org.setNom("OFPPT");

    when(organismeService.getOrganismeById(1L))
            .thenReturn(Optional.of(org));

    mockMvc.perform(get("/api/organismes/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetOrganismeById_NonTrouve() throws Exception {
    when(organismeService.getOrganismeById(99L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/organismes/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testGetOrganismeByCode_Trouve() throws Exception {
    Organisme org = new Organisme();
    org.setCode("ORG001");

    when(organismeService.getOrganismeByCode("ORG001"))
            .thenReturn(Optional.of(org));

    mockMvc.perform(get("/api/organismes/code/ORG001"))
            .andExpect(status().isOk());
  }

  @Test
  void testCreateOrganisme() throws Exception {
    Organisme org = new Organisme();
    org.setNom("OFPPT");

    when(organismeService.saveOrganisme(any(Organisme.class)))
            .thenReturn(org);

    mockMvc.perform(post("/api/organismes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"OFPPT\",\"code\":\"ORG001\"}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testUpdateOrganisme_Trouve() throws Exception {
    Organisme org = new Organisme();
    org.setNom("OFPPT modifié");

    when(organismeService.updateOrganisme(eq(1L), any(Organisme.class)))
            .thenReturn(org);

    mockMvc.perform(put("/api/organismes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"OFPPT modifié\"}"))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdateOrganisme_NonTrouve() throws Exception {
    when(organismeService.updateOrganisme(eq(99L), any(Organisme.class)))
            .thenReturn(null);

    mockMvc.perform(put("/api/organismes/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"test\"}"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteOrganisme() throws Exception {
    doNothing().when(organismeService).deleteOrganisme(1L);

    mockMvc.perform(delete("/api/organismes/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testSearchOrganismesByNom() throws Exception {
    when(organismeService.searchOrganismesByNom("OFPPT"))
            .thenReturn(Arrays.asList(new Organisme()));

    mockMvc.perform(get("/api/organismes/search")
                    .param("nom", "OFPPT"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetProjetsByOrganisme() throws Exception {
    when(organismeService.getProjetsByOrganisme(1L))
            .thenReturn(Arrays.asList(new Projet()));

    mockMvc.perform(get("/api/organismes/1/projets"))
            .andExpect(status().isOk());
  }

  @Test
  void testCountProjetsByOrganisme() throws Exception {
    when(organismeService.countProjetsByOrganisme(1L)).thenReturn(3L);

    mockMvc.perform(get("/api/organismes/1/projets/count"))
            .andExpect(status().isOk())
            .andExpect(content().string("3"));
  }

  @Test
  void testHasProjetsEnCours() throws Exception {
    when(organismeService.hasProjetsEnCours(1L)).thenReturn(true);

    mockMvc.perform(get("/api/organismes/1/projets/en-cours"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
  }
}