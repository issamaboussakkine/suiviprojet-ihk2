package ma.projet.ihk.suiviprojet_ihk.services;

import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.repositories.FactureRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import ma.projet.ihk.suiviprojet_ihk.service.FactureServiceImpl;
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
 * Tests unitaires pour FactureServiceImpl
 * Teste la logique métier de la facturation sans base de données
 */
@ExtendWith(MockitoExtension.class)
public class FactureServiceTest {

  @Mock
  private FactureRepository factureRepository;

  @Mock
  private PhaseRepository phaseRepository;

  @InjectMocks
  private FactureServiceImpl factureService;

  @Test
  void testSaveFacture_Nouvelle() {
    Phase phase = new Phase();
    phase.setId(1);

    Facture facture = new Facture();
    facture.setDateFacture(LocalDate.now());
    facture.setPhase(phase);

    when(factureRepository.findByPhaseId(1)).thenReturn(null);
    when(factureRepository.save(facture)).thenReturn(facture);

    Facture result = factureService.saveFacture(facture);

    assertNotNull(result);
    assertNotNull(result.getDateFacture());
    verify(factureRepository, times(1)).save(facture);
  }

  @Test
  void testSaveFacture_PhaseDejaFacturee() {
    Phase phase = new Phase();
    phase.setId(1);

    Facture factureExistante = new Facture();
    factureExistante.setDateFacture(LocalDate.now());

    Facture nouvelleFacture = new Facture();
    nouvelleFacture.setPhase(phase);

    when(factureRepository.findByPhaseId(1)).thenReturn(factureExistante);

    assertThrows(RuntimeException.class, () -> {
      factureService.saveFacture(nouvelleFacture);
    });
  }

  @Test
  void testUpdateFacture_Existe() {
    Facture facture = new Facture();
    facture.setDateFacture(LocalDate.now());

    when(factureRepository.existsById(1)).thenReturn(true);
    when(factureRepository.save(any(Facture.class))).thenReturn(facture);

    Facture result = factureService.updateFacture(1L, facture);

    assertNotNull(result);
    assertNotNull(result.getDateFacture());
  }

  @Test
  void testUpdateFacture_NonTrouve() {
    Facture facture = new Facture();
    when(factureRepository.existsById(99)).thenReturn(false);

    Facture result = factureService.updateFacture(99L, facture);

    assertNull(result);
  }

  @Test
  void testGetFactureById() {
    Facture facture = new Facture();
    facture.setId(1);
    facture.setDateFacture(LocalDate.now());

    when(factureRepository.findById(1)).thenReturn(Optional.of(facture));

    Optional<Facture> result = factureService.getFactureById(1L);

    assertTrue(result.isPresent());
    assertNotNull(result.get().getDateFacture());
  }

  @Test
  void testGetAllFactures() {
    Facture f1 = new Facture();
    f1.setDateFacture(LocalDate.now());
    Facture f2 = new Facture();
    f2.setDateFacture(LocalDate.now().minusDays(5));

    when(factureRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

    List<Facture> result = factureService.getAllFactures();

    assertEquals(2, result.size());
  }

  @Test
  void testDeleteFacture() {
    doNothing().when(factureRepository).deleteById(1);

    factureService.deleteFacture(1L);

    verify(factureRepository, times(1)).deleteById(1);
  }

  @Test
  void testGetFacturesByPeriode() {
    LocalDate debut = LocalDate.now().minusMonths(1);
    LocalDate fin = LocalDate.now();

    Facture f1 = new Facture();
    f1.setDateFacture(LocalDate.now().minusDays(15));

    when(factureRepository.findByDateFactureBetween(debut, fin))
            .thenReturn(Arrays.asList(f1));

    List<Facture> result = factureService.getFacturesByPeriode(debut, fin);

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
  }

  @Test
  void testPhaseDejaFacturee_Vrai() {
    Facture facture = new Facture();
    when(factureRepository.findByPhaseId(1)).thenReturn(facture);

    boolean result = factureService.phaseDejaFacturee(1L);

    assertTrue(result);
  }

  @Test
  void testPhaseDejaFacturee_Faux() {
    when(factureRepository.findByPhaseId(1)).thenReturn(null);

    boolean result = factureService.phaseDejaFacturee(1L);

    assertFalse(result);
  }

  @Test
  void testCountFacturesByPeriode() {
    LocalDate debut = LocalDate.now().minusMonths(1);
    LocalDate fin = LocalDate.now();

    Facture f1 = new Facture();
    Facture f2 = new Facture();

    when(factureRepository.findByDateFactureBetween(debut, fin))
            .thenReturn(Arrays.asList(f1, f2));

    long count = factureService.countFacturesByPeriode(debut, fin);

    assertEquals(2, count);
  }
}