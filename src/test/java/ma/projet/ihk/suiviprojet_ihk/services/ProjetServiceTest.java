package ma.projet.ihk.suiviprojet_ihk.services;

import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProjetRepository;
import ma.projet.ihk.suiviprojet_ihk.service.ProjetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour ProjetServiceImpl
 * Utilise Mockito pour simuler ProjetRepository sans base de données
 */
@ExtendWith(MockitoExtension.class)
public class ProjetServiceTest {

  @Mock
  private ProjetRepository projetRepository;

  @InjectMocks
  private ProjetServiceImpl projetService;

  @Test
  void testSaveProjet() {
    // ARRANGE
    Projet projet = new Projet();
    projet.setCode("PRJ001");
    projet.setNom("Système de suivi");
    projet.setMontant(150000.0);

    when(projetRepository.save(projet)).thenReturn(projet);

    // ACT
    Projet result = projetService.saveProjet(projet);

    // ASSERT
    assertNotNull(result);
    assertEquals("PRJ001", result.getCode());
    verify(projetRepository, times(1)).save(projet);
  }

  @Test
  void testUpdateProjet_Existe() {
    // ARRANGE
    Projet projet = new Projet();
    projet.setNom("Projet modifié");
    projet.setMontant(200000.0);

    when(projetRepository.existsById(1)).thenReturn(true);
    when(projetRepository.save(any(Projet.class))).thenReturn(projet);

    // ACT
    Projet result = projetService.updateProjet(1L, projet);

    // ASSERT
    assertNotNull(result);
    assertEquals("Projet modifié", result.getNom());
  }

  @Test
  void testUpdateProjet_NonTrouve() {
    // ARRANGE
    Projet projet = new Projet();
    projet.setNom("Projet inexistant");

    when(projetRepository.existsById(99)).thenReturn(false);

    // ACT
    Projet result = projetService.updateProjet(99L, projet);

    // ASSERT → doit retourner null
    assertNull(result);
  }

  @Test
  void testGetProjetById() {
    // ARRANGE
    Projet projet = new Projet();
    projet.setId(1);
    projet.setNom("Projet Test");

    when(projetRepository.findById(1)).thenReturn(Optional.of(projet));

    // ACT
    Optional<Projet> result = projetService.getProjetById(1L);

    // ASSERT
    assertTrue(result.isPresent());
    assertEquals("Projet Test", result.get().getNom());
  }

  @Test
  void testGetAllProjets() {
    // ARRANGE
    Projet p1 = new Projet();
    p1.setNom("Projet 1");
    Projet p2 = new Projet();
    p2.setNom("Projet 2");

    when(projetRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

    // ACT
    List<Projet> result = projetService.getAllProjets();

    // ASSERT
    assertEquals(2, result.size());
  }

  @Test
  void testDeleteProjet() {
    // ARRANGE
    doNothing().when(projetRepository).deleteById(1);

    // ACT
    projetService.deleteProjet(1L);

    // ASSERT
    verify(projetRepository, times(1)).deleteById(1);
  }

  @Test
  void testGetMontantTotalProjets() {
    // ARRANGE
    Projet p1 = new Projet();
    p1.setMontant(100000.0);
    Projet p2 = new Projet();
    p2.setMontant(50000.0);

    when(projetRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

    // ACT
    double total = projetService.getMontantTotalProjets();

    // ASSERT
    assertEquals(150000.0, total);
  }

  @Test
  void testCountProjetsEnCours() {
    // ARRANGE
    Projet p1 = new Projet();
    p1.setDateDebut(LocalDate.now().minusMonths(1));
    p1.setDateFin(LocalDate.now().plusMonths(5)); // en cours

    Projet p2 = new Projet();
    p2.setDateDebut(LocalDate.now().minusMonths(6));
    p2.setDateFin(LocalDate.now().minusDays(1)); // terminé

    when(projetRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

    // ACT
    long count = projetService.countProjetsEnCours();

    // ASSERT
    assertEquals(1, count);
  }

  @Test
  void testCountProjetsTermines() {
    // ARRANGE
    Projet p1 = new Projet();
    p1.setDateFin(LocalDate.now().minusDays(1)); // terminé

    Projet p2 = new Projet();
    p2.setDateFin(LocalDate.now().plusMonths(3)); // en cours

    when(projetRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

    // ACT
    long count = projetService.countProjetsTermines();

    // ASSERT
    assertEquals(1, count);
  }
}