package ma.projet.ihk.suiviprojet_ihk.services;

import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.repositories.AffectationRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.EmployeRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import ma.projet.ihk.suiviprojet_ihk.service.AffectationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AffectationServiceTest {

  @Mock
  private AffectationRepository affectationRepository;

  @Mock
  private EmployeRepository employeRepository;

  @Mock
  private PhaseRepository phaseRepository;

  @InjectMocks
  private AffectationServiceImpl affectationService;

  @Test
  void testSaveAffectation_EmployeDisponible() {
    Employe emp = new Employe();
    emp.setId(1);

    Affectation affectation = new Affectation();
    affectation.setEmploye(emp);
    affectation.setDateDebut(LocalDate.now());
    affectation.setDateFin(LocalDate.now().plusMonths(1));

    when(affectationRepository.findByEmployeId(1))
            .thenReturn(Collections.emptyList());
    when(affectationRepository.save(affectation)).thenReturn(affectation);

    Affectation result = affectationService.saveAffectation(affectation);

    assertNotNull(result);
    verify(affectationRepository, times(1)).save(affectation);
  }

  @Test
  void testSaveAffectation_EmployeNonDisponible() {
    Employe emp = new Employe();
    emp.setId(1);

    // Affectation existante qui chevauche
    Affectation existante = new Affectation();
    existante.setDateDebut(LocalDate.now().minusDays(5));
    existante.setDateFin(LocalDate.now().plusDays(10));

    Affectation nouvelle = new Affectation();
    nouvelle.setEmploye(emp);
    nouvelle.setDateDebut(LocalDate.now());
    nouvelle.setDateFin(LocalDate.now().plusDays(5));

    when(affectationRepository.findByEmployeId(1))
            .thenReturn(Arrays.asList(existante));

    assertThrows(RuntimeException.class, () -> {
      affectationService.saveAffectation(nouvelle);
    });
  }

  @Test
  void testGetAffectationsByPhase() {
    Affectation a1 = new Affectation();
    Affectation a2 = new Affectation();

    when(affectationRepository.findByPhaseId(1))
            .thenReturn(Arrays.asList(a1, a2));

    List<Affectation> result = affectationService.getAffectationsByPhase(1L);

    assertEquals(2, result.size());
  }

  @Test
  void testGetAffectationsByEmploye() {
    Affectation a1 = new Affectation();

    when(affectationRepository.findByEmployeId(1))
            .thenReturn(Arrays.asList(a1));

    List<Affectation> result = affectationService.getAffectationsByEmploye(1L);

    assertFalse(result.isEmpty());
  }

  @Test
  void testDeleteAffectation() {
    AffectationId id = new AffectationId(1, 1);
    doNothing().when(affectationRepository).deleteById(id);

    affectationService.deleteAffectation(id);

    verify(affectationRepository, times(1)).deleteById(id);
  }

  @Test
  void testIsEmployeDisponible_Vrai() {
    when(affectationRepository.findByEmployeId(1))
            .thenReturn(Collections.emptyList());

    boolean result = affectationService.isEmployeDisponible(
            1L,
            LocalDate.now(),
            LocalDate.now().plusMonths(1)
    );

    assertTrue(result);
  }

  @Test
  void testIsEmployeDisponible_Faux() {
    Affectation existante = new Affectation();
    existante.setDateDebut(LocalDate.now().minusDays(5));
    existante.setDateFin(LocalDate.now().plusDays(10));

    when(affectationRepository.findByEmployeId(1))
            .thenReturn(Arrays.asList(existante));

    boolean result = affectationService.isEmployeDisponible(
            1L,
            LocalDate.now(),
            LocalDate.now().plusDays(5)
    );

    assertFalse(result);
  }

  @Test
  void testCountEmployesByPhase() {
    Affectation a1 = new Affectation();
    Affectation a2 = new Affectation();

    when(affectationRepository.findByPhaseId(1))
            .thenReturn(Arrays.asList(a1, a2));

    long count = affectationService.countEmployesByPhase(1L);

    assertEquals(2, count);
  }

  @Test
  void testCountPhasesByEmploye() {
    Affectation a1 = new Affectation();

    when(affectationRepository.findByEmployeId(1))
            .thenReturn(Arrays.asList(a1));

    long count = affectationService.countPhasesByEmploye(1L);

    assertEquals(1, count);
  }
}