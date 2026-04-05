package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import ma.projet.ihk.suiviprojet_ihk.service.LivrableService;
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

@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.LivrableController.class)
public class LivrableControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private LivrableService livrableService;

  @Test
  void testGetAllLivrables() throws Exception {
    when(livrableService.getAllLivrables())
            .thenReturn(Arrays.asList(new Livrable(), new Livrable()));

    mockMvc.perform(get("/api/livrables"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetLivrableById_Trouve() throws Exception {
    Livrable livrable = new Livrable();
    livrable.setId(1);

    when(livrableService.getLivrableById(1L))
            .thenReturn(Optional.of(livrable));

    mockMvc.perform(get("/api/livrables/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetLivrableById_NonTrouve() throws Exception {
    when(livrableService.getLivrableById(99L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/livrables/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testCreateLivrable() throws Exception {
    Livrable livrable = new Livrable();
    livrable.setLibelle("CDC");

    when(livrableService.saveLivrable(any(Livrable.class)))
            .thenReturn(livrable);

    mockMvc.perform(post("/api/livrables")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"CDC\",\"code\":\"LIV001\"}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testUpdateLivrable_Trouve() throws Exception {
    Livrable livrable = new Livrable();
    livrable.setLibelle("Livrable modifié");

    when(livrableService.updateLivrable(eq(1L), any(Livrable.class)))
            .thenReturn(livrable);

    mockMvc.perform(put("/api/livrables/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"Livrable modifié\"}"))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdateLivrable_NonTrouve() throws Exception {
    when(livrableService.updateLivrable(eq(99L), any(Livrable.class)))
            .thenReturn(null);

    mockMvc.perform(put("/api/livrables/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"libelle\":\"test\"}"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteLivrable() throws Exception {
    doNothing().when(livrableService).deleteLivrable(1L);

    mockMvc.perform(delete("/api/livrables/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetLivrablesByPhase() throws Exception {
    when(livrableService.getLivrablesByPhase(1L))
            .thenReturn(Arrays.asList(new Livrable()));

    mockMvc.perform(get("/api/livrables/phase/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testCountLivrablesByPhase() throws Exception {
    when(livrableService.countLivrablesByPhase(1L)).thenReturn(3L);

    mockMvc.perform(get("/api/livrables/phase/1/count"))
            .andExpect(status().isOk())
            .andExpect(content().string("3"));
  }

  @Test
  void testHasLivrables() throws Exception {
    when(livrableService.hasLivrables(1L)).thenReturn(true);

    mockMvc.perform(get("/api/livrables/phase/1/exists"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
  }
}