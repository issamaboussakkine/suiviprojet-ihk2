package ma.projet.ihk.suiviprojet_ihk.services;


import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import ma.projet.ihk.suiviprojet_ihk.repositories.AffectationRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.EmployeRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProfilRepository;
import ma.projet.ihk.suiviprojet_ihk.service.EmployeServiceImpl;
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

/**
 * Tests unitaires pour EmployeServiceImpl
 * Teste la logique métier des employés sans base de données
 */
@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

  @Mock
  private EmployeRepository employeRepository;

  @Mock
  private ProfilRepository profilRepository;

  @Mock
  private AffectationRepository affectationRepository;

  @InjectMocks
  private EmployeServiceImpl employeService;

  @Test
  void testSaveEmploye() {
    Employe emp = new Employe();
    emp.setMatricule("EMP001");
    emp.setNom("Alami");
    emp.setLogin("alami");

    when(employeRepository.save(emp)).thenReturn(emp);

    Employe result = employeService.saveEmploye(emp);

    assertNotNull(result);
    assertEquals("Alami", result.getNom());
    verify(employeRepository, times(1)).save(emp);
  }

  @Test
  void testUpdateEmploye_Existe() {
    Employe emp = new Employe();
    emp.setNom("Alami modifié");

    when(employeRepository.existsById(1)).thenReturn(true);
    when(employeRepository.save(any(Employe.class))).thenReturn(emp);

    Employe result = employeService.updateEmploye(1L, emp);

    assertNotNull(result);
    assertEquals("Alami modifié", result.getNom());
  }

  @Test
  void testUpdateEmploye_NonTrouve() {
    Employe emp = new Employe();
    when(employeRepository.existsById(99)).thenReturn(false);

    Employe result = employeService.updateEmploye(99L, emp);

    assertNull(result);
  }

  @Test
  void testGetEmployeById() {
    Employe emp = new Employe();
    emp.setId(1);
    emp.setNom("Benali");

    when(employeRepository.findById(1)).thenReturn(Optional.of(emp));

    Optional<Employe> result = employeService.getEmployeById(1L);

    assertTrue(result.isPresent());
    assertEquals("Benali", result.get().getNom());
  }

  @Test
  void testGetEmployeByMatricule() {
    Employe emp = new Employe();
    emp.setMatricule("EMP001");
    emp.setNom("Alami");

    when(employeRepository.findByMatricule("EMP001")).thenReturn(emp);

    Optional<Employe> result = employeService.getEmployeByMatricule("EMP001");

    assertTrue(result.isPresent());
    assertEquals("Alami", result.get().getNom());
  }

  @Test
  void testGetEmployeByLogin() {
    Employe emp = new Employe();
    emp.setLogin("alami");
    emp.setNom("Alami");

    when(employeRepository.findByLogin("alami")).thenReturn(emp);

    Optional<Employe> result = employeService.getEmployeByLogin("alami");

    assertTrue(result.isPresent());
    assertEquals("Alami", result.get().getNom());
  }

  @Test
  void testGetAllEmployes() {
    Employe emp1 = new Employe();
    emp1.setNom("Alami");
    Employe emp2 = new Employe();
    emp2.setNom("Benali");

    when(employeRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2));

    List<Employe> result = employeService.getAllEmployes();

    assertEquals(2, result.size());
  }

  @Test
  void testDeleteEmploye() {
    doNothing().when(employeRepository).deleteById(1);

    employeService.deleteEmploye(1L);

    verify(employeRepository, times(1)).deleteById(1);
  }

  @Test
  void testAuthentifier_Succes() {
    Employe emp = new Employe();
    emp.setLogin("alami");
    emp.setPassword("azerty");

    when(employeRepository.findByLogin("alami")).thenReturn(emp);

    Employe result = employeService.authentifier("alami", "azerty");

    assertNotNull(result);
    assertEquals("alami", result.getLogin());
  }

  @Test
  void testAuthentifier_Echec() {
    Employe emp = new Employe();
    emp.setLogin("alami");
    emp.setPassword("azerty");

    when(employeRepository.findByLogin("alami")).thenReturn(emp);

    Employe result = employeService.authentifier("alami", "mauvaispass");

    assertNull(result);
  }

  @Test
  void testSearchEmployesByNom() {
    Employe emp1 = new Employe();
    emp1.setNom("Alami");
    Employe emp2 = new Employe();
    emp2.setNom("Benali");

    when(employeRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2));

    List<Employe> result = employeService.searchEmployesByNom("Alami");

    assertEquals(1, result.size());
    assertEquals("Alami", result.get(0).getNom());
  }

  @Test
  void testGetEmployesByProfil() {
    Employe emp = new Employe();
    emp.setNom("Alami");

    when(employeRepository.findByProfilId(1)).thenReturn(Arrays.asList(emp));

    List<Employe> result = employeService.getEmployesByProfil(1L);

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
  }
}