package ma.projet.ihk.suiviprojet_ihk.services;

import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import ma.projet.ihk.suiviprojet_ihk.repositories.LivrableRepository;
import ma.projet.ihk.suiviprojet_ihk.service.LivrableServiceImpl;
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

@ExtendWith(MockitoExtension.class)
public class LivrableServiceTest {

  @Mock
  private LivrableRepository livrableRepository;

  @InjectMocks
  private LivrableServiceImpl livrableService;

  @Test
  void testSaveLivrable() {
    Livrable livrable = new Livrable();
    livrable.setCode("LIV001");
    livrable.setLibelle("Cahier des charges");

    when(livrableRepository.save(livrable)).thenReturn(livrable);

    Livrable result = livrableService.saveLivrable(livrable);

    assertNotNull(result);
    assertEquals("Cahier des charges", result.getLibelle());
    verify(livrableRepository, times(1)).save(livrable);
  }

  @Test
  void testUpdateLivrable_Existe() {
    Livrable livrable = new Livrable();
    livrable.setLibelle("Livrable modifié");

    when(livrableRepository.existsById(1)).thenReturn(true);
    when(livrableRepository.save(any(Livrable.class))).thenReturn(livrable);

    Livrable result = livrableService.updateLivrable(1L, livrable);

    assertNotNull(result);
    assertEquals("Livrable modifié", result.getLibelle());
  }

  @Test
  void testUpdateLivrable_NonTrouve() {
    Livrable livrable = new Livrable();
    when(livrableRepository.existsById(99)).thenReturn(false);

    Livrable result = livrableService.updateLivrable(99L, livrable);

    assertNull(result);
  }

  @Test
  void testGetLivrableById() {
    Livrable livrable = new Livrable();
    livrable.setId(1);
    livrable.setLibelle("Specs");

    when(livrableRepository.findById(1)).thenReturn(Optional.of(livrable));

    Optional<Livrable> result = livrableService.getLivrableById(1L);

    assertTrue(result.isPresent());
    assertEquals("Specs", result.get().getLibelle());
  }

  @Test
  void testGetLivrablesByPhase() {
    Livrable l1 = new Livrable();
    l1.setLibelle("Doc1");
    Livrable l2 = new Livrable();
    l2.setLibelle("Doc2");

    when(livrableRepository.findByPhaseId(1)).thenReturn(Arrays.asList(l1, l2));

    List<Livrable> result = livrableService.getLivrablesByPhase(1L);

    assertEquals(2, result.size());
  }

  @Test
  void testDeleteLivrable() {
    doNothing().when(livrableRepository).deleteById(1);

    livrableService.deleteLivrable(1L);

    verify(livrableRepository, times(1)).deleteById(1);
  }

  @Test
  void testHasLivrables_Vrai() {
    Livrable livrable = new Livrable();
    when(livrableRepository.findByPhaseId(1)).thenReturn(Arrays.asList(livrable));

    boolean result = livrableService.hasLivrables(1L);

    assertTrue(result);
  }

  @Test
  void testHasLivrables_Faux() {
    when(livrableRepository.findByPhaseId(1)).thenReturn(Arrays.asList());

    boolean result = livrableService.hasLivrables(1L);

    assertFalse(result);
  }

  @Test
  void testCountLivrablesByPhase() {
    Livrable l1 = new Livrable();
    Livrable l2 = new Livrable();

    when(livrableRepository.findByPhaseId(1)).thenReturn(Arrays.asList(l1, l2));

    long count = livrableService.countLivrablesByPhase(1L);

    assertEquals(2, count);
  }
}