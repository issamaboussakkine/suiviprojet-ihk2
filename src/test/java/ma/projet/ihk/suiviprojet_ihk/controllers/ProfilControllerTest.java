package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import ma.projet.ihk.suiviprojet_ihk.service.ProfilService;
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

@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.ProfilController.class)
public class ProfilControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProfilService profilService;

  @Test
  void testGetAllProfils() throws Exception {
    when(profilService.getAllProfils())
            .thenReturn(Arrays.asList(new Profil(), new Profil()));

    mockMvc.perform(get("/api/profils"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetProfilById_Trouve() throws Exception {
    Profil profil = new Profil();
    profil.setId(1);
    profil.setCode("CP");

    when(profilService.getProfilById(1L))
            .thenReturn(Optional.of(profil));

    mockMvc.perform(get("/api/profils/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetProfilById_NonTrouve() throws Exception {
    when(profilService.getProfilById(99L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/profils/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testGetProfilByCode_Trouve() throws Exception {
    Profil profil = new Profil();
    profil.setCode("CP");

    when(profilService.getProfilByCode("CP"))
            .thenReturn(Optional.of(profil));

    mockMvc.perform(get("/api/profils/code/CP"))
            .andExpect(status().isOk());
  }

  @Test
  void testCreateProfil_CodeUnique() throws Exception {
    Profil profil = new Profil();
    profil.setCode("CP");
    profil.setLibelle("Chef de projet");

    when(profilService.isCodeUnique("CP")).thenReturn(true);
    when(profilService.saveProfil(any(Profil.class))).thenReturn(profil);

    mockMvc.perform(post("/api/profils")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"code\":\"CP\",\"libelle\":\"Chef de projet\"}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testCreateProfil_CodeDuplique() throws Exception {
    when(profilService.isCodeUnique("CP")).thenReturn(false);

    mockMvc.perform(post("/api/profils")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"code\":\"CP\",\"libelle\":\"Chef de projet\"}"))
            .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateProfil_Trouve() throws Exception {
    Profil profil = new Profil();
    profil.setLibelle("Chef de projet modifié");

    when(profilService.updateProfil(eq(1L), any(Profil.class)))
            .thenReturn(profil);

    mockMvc.perform(put("/api/profils/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"code\":\"CP\",\"libelle\":\"Chef de projet modifié\"}"))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdateProfil_NonTrouve() throws Exception {
    when(profilService.updateProfil(eq(99L), any(Profil.class)))
            .thenReturn(null);

    mockMvc.perform(put("/api/profils/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"code\":\"CP\"}"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteProfil() throws Exception {
    doNothing().when(profilService).deleteProfil(1L);

    mockMvc.perform(delete("/api/profils/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetAllCodes() throws Exception {
    when(profilService.getAllCodes())
            .thenReturn(Arrays.asList("CP", "COMPT", "SEC"));

    mockMvc.perform(get("/api/profils/codes"))
            .andExpect(status().isOk());
  }
}