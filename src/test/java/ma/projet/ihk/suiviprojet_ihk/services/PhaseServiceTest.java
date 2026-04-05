package ma.projet.ihk.suiviprojet_ihk.services;

import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProjetRepository;
import ma.projet.ihk.suiviprojet_ihk.service.PhaseServiceImpl;
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
 * Tests unitaires pour PhaseServiceImpl
 * Teste la logique métier des phases sans base de données
 */
@ExtendWith(MockitoExtension.class)
public class PhaseServiceTest {

  @Mock
  private PhaseRepository phaseRepository;

  @Mock
  private ProjetRepository projetRepository;

  @InjectMocks
  private PhaseServiceImpl phaseService;

  @Test
  void testSavePhase() {
    Phase phase = new Phase();
    phase.setCode("PH001");
    phase.setLibelle("Analyse");
    phase.setMontant(20000.0);

    when(phaseRepository.save(phase)).thenReturn(phase);

    Phase result = phaseService.savePhase(phase);

    assertNotNull(result);
    assertEquals("Analyse", result.getLibelle());
    verify(phaseRepository, times(1)).save(phase);
  }

  @Test
  void testUpdatePhase_Existe() {
    Phase phase = new Phase();
    phase.setLibelle("Conception modifiée");

    when(phaseRepository.existsById(1)).thenReturn(true);
    when(phaseRepository.save(any(Phase.class))).thenReturn(phase);

    Phase result = phaseService.updatePhase(1L, phase);

    assertNotNull(result);
    assertEquals("Conception modifiée", result.getLibelle());
  }

  @Test
  void testUpdatePhase_NonTrouve() {
    Phase phase = new Phase();
    when(phaseRepository.existsById(99)).thenReturn(false);

    Phase result = phaseService.updatePhase(99L, phase);

    assertNull(result);
  }

  @Test
  void testGetPhaseById() {
    Phase phase = new Phase();
    phase.setId(1);
    phase.setLibelle("Développement");

    when(phaseRepository.findById(1)).thenReturn(Optional.of(phase));

    Optional<Phase> result = phaseService.getPhaseById(1L);

    assertTrue(result.isPresent());
    assertEquals("Développement", result.get().getLibelle());
  }

  @Test
  void testGetPhasesTermineesNonFacturees() {
    Phase phase = new Phase();
    phase.setEtatRealisation(true);
    phase.setEtatFacturation(false);

    when(phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse())
            .thenReturn(Arrays.asList(phase));

    List<Phase> result = phaseService.getPhasesTermineesNonFacturees();

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
  }

  @Test
  void testGetPhasesFactureesNonPayees() {
    Phase phase = new Phase();
    phase.setEtatFacturation(true);
    phase.setEtatPaiement(false);

    when(phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse())
            .thenReturn(Arrays.asList(phase));

    List<Phase> result = phaseService.getPhasesFactureesNonPayees();

    assertFalse(result.isEmpty());
  }

  @Test
  void testGetPhasesPayees() {
    Phase phase = new Phase();
    phase.setEtatPaiement(true);

    when(phaseRepository.findByEtatPaiementTrue())
            .thenReturn(Arrays.asList(phase));

    List<Phase> result = phaseService.getPhasesPayees();

    assertFalse(result.isEmpty());
  }

  @Test
  void testMarquerTerminee() {
    Phase phase = new Phase();
    phase.setId(1);
    phase.setEtatRealisation(false);

    when(phaseRepository.findById(1)).thenReturn(Optional.of(phase));
    when(phaseRepository.save(any(Phase.class))).thenReturn(phase);

    Phase result = phaseService.marquerTerminee(1L);

    assertNotNull(result);
    assertTrue(result.isEtatRealisation());
  }

  @Test
  void testMarquerFacturee() {
    Phase phase = new Phase();
    phase.setId(1);
    phase.setEtatFacturation(false);

    when(phaseRepository.findById(1)).thenReturn(Optional.of(phase));
    when(phaseRepository.save(any(Phase.class))).thenReturn(phase);

    Phase result = phaseService.marquerFacturee(1L);

    assertNotNull(result);
    assertTrue(result.isEtatFacturation());
  }

  @Test
  void testMarquerPayee() {
    Phase phase = new Phase();
    phase.setId(1);
    phase.setEtatPaiement(false);

    when(phaseRepository.findById(1)).thenReturn(Optional.of(phase));
    when(phaseRepository.save(any(Phase.class))).thenReturn(phase);

    Phase result = phaseService.marquerPayee(1L);

    assertNotNull(result);
    assertTrue(result.isEtatPaiement());
  }

  @Test
  void testGetMontantTotalPhasesByProjet() {
    Phase p1 = new Phase();
    p1.setMontant(30000.0);
    Phase p2 = new Phase();
    p2.setMontant(20000.0);

    when(phaseRepository.findByProjetId(1))
            .thenReturn(Arrays.asList(p1, p2));

    double total = phaseService.getMontantTotalPhasesByProjet(1L);

    assertEquals(50000.0, total);
  }

  @Test
  void testCountPhasesEnCoursByProjet() {
    Phase p1 = new Phase();
    p1.setEtatRealisation(false);
    p1.setDateFin(LocalDate.now().plusMonths(2)); // en cours

    Phase p2 = new Phase();
    p2.setEtatRealisation(true); // terminée

    when(phaseRepository.findByProjetId(1))
            .thenReturn(Arrays.asList(p1, p2));

    long count = phaseService.countPhasesEnCoursByProjet(1L);

    assertEquals(1, count);
  }
}