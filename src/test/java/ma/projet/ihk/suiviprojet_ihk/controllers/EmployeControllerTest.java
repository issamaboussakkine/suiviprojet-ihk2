package ma.projet.ihk.suiviprojet_ihk.controllers;

import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.service.EmployeService;
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
 * Tests unitaires pour EmployeController
 * Teste les endpoints REST de gestion des employés
 */
@WebMvcTest(controllers = ma.projet.ihk.suiviprojet_ihk.controller.EmployeController.class)
public class EmployeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private EmployeService employeService;

  @Test
  void testGetAllEmployes() throws Exception {
    when(employeService.getAllEmployes())
            .thenReturn(Arrays.asList(new Employe(), new Employe()));

    mockMvc.perform(get("/api/employes"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetEmployeById_Trouve() throws Exception {
    Employe emp = new Employe();
    emp.setId(1);
    emp.setNom("Alami");

    when(employeService.getEmployeById(1L))
            .thenReturn(Optional.of(emp));

    mockMvc.perform(get("/api/employes/1"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetEmployeById_NonTrouve() throws Exception {
    when(employeService.getEmployeById(99L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/api/employes/99"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testCreateEmploye() throws Exception {
    Employe emp = new Employe();
    emp.setNom("Alami");

    when(employeService.saveEmploye(any(Employe.class)))
            .thenReturn(emp);

    mockMvc.perform(post("/api/employes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"Alami\",\"prenom\":\"Samir\",\"login\":\"alami\",\"password\":\"azerty\"}"))
            .andExpect(status().isCreated());
  }

  @Test
  void testUpdateEmploye_Trouve() throws Exception {
    Employe emp = new Employe();
    emp.setNom("Alami modifié");

    when(employeService.updateEmploye(eq(1L), any(Employe.class)))
            .thenReturn(emp);

    mockMvc.perform(put("/api/employes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"Alami modifié\"}"))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdateEmploye_NonTrouve() throws Exception {
    when(employeService.updateEmploye(eq(99L), any(Employe.class)))
            .thenReturn(null);

    mockMvc.perform(put("/api/employes/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"nom\":\"test\"}"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteEmploye() throws Exception {
    doNothing().when(employeService).deleteEmploye(1L);

    mockMvc.perform(delete("/api/employes/1"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetByMatricule_Trouve() throws Exception {
    Employe emp = new Employe();
    emp.setMatricule("EMP001");

    when(employeService.getEmployeByMatricule("EMP001"))
            .thenReturn(Optional.of(emp));

    mockMvc.perform(get("/api/employes/matricule/EMP001"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetByLogin_Trouve() throws Exception {
    Employe emp = new Employe();
    emp.setLogin("alami");

    when(employeService.getEmployeByLogin("alami"))
            .thenReturn(Optional.of(emp));

    mockMvc.perform(get("/api/employes/login/alami"))
            .andExpect(status().isOk());
  }

  @Test
  void testLogin_Succes() throws Exception {
    Employe emp = new Employe();
    emp.setLogin("alami");

    when(employeService.authentifier("alami", "azerty"))
            .thenReturn(emp);

    mockMvc.perform(post("/api/employes/login")
                    .param("login", "alami")
                    .param("password", "azerty"))
            .andExpect(status().isOk());
  }

  @Test
  void testLogin_Echec() throws Exception {
    when(employeService.authentifier("alami", "mauvais"))
            .thenReturn(null);

    mockMvc.perform(post("/api/employes/login")
                    .param("login", "alami")
                    .param("password", "mauvais"))
            .andExpect(status().isUnauthorized());
  }
}