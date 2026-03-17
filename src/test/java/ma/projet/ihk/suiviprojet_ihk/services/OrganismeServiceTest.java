package ma.projet.ihk.suiviprojet_ihk.services;

import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.repositories.OrganismeRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProjetRepository;
import ma.projet.ihk.suiviprojet_ihk.service.OrganismeServiceImpl;
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
 * Tests unitaires pour OrganismeServiceImpl
 * @ExtendWith(MockitoExtension.class) → pas besoin de base de données
 * @Mock → simule le repository sans toucher MySQL
 * @InjectMocks → injecte les mocks dans le service
 */
@ExtendWith(MockitoExtension.class)
public class OrganismeServiceTest {

  @Mock
  private OrganismeRepository organismeRepository;

  @Mock
  private ProjetRepository projetRepository;

  @InjectMocks
  private OrganismeServiceImpl organismeService;

  @Test
  void testSaveOrganisme() {
    // ARRANGE
    Organisme org = new Organisme();
    org.setCode("ORG001");
    org.setNom("OFPPT");

    when(organismeRepository.save(org)).thenReturn(org);

    // ACT
    Organisme result = organismeService.saveOrganisme(org);

    // ASSERT
    assertNotNull(result);
    assertEquals("OFPPT", result.getNom());
    verify(organismeRepository, times(1)).save(org);
  }

  @Test
  void testUpdateOrganisme() {
    // ARRANGE
    Organisme org = new Organisme();
    org.setCode("ORG001");
    org.setNom("OFPPT modifié");

    when(organismeRepository.existsById(1)).thenReturn(true);
    when(organismeRepository.save(any(Organisme.class))).thenReturn(org);

    // ACT
    Organisme result = organismeService.updateOrganisme(1L, org);

    // ASSERT
    assertNotNull(result);
    assertEquals("OFPPT modifié", result.getNom());
  }

  @Test
  void testUpdateOrganisme_NotFound() {
    // ARRANGE
    Organisme org = new Organisme();
    org.setNom("OFPPT");

    when(organismeRepository.existsById(99)).thenReturn(false);

    // ACT
    Organisme result = organismeService.updateOrganisme(99L, org);

    // ASSERT → doit retourner null si non trouvé
    assertNull(result);
  }

  @Test
  void testGetOrganismeById() {
    // ARRANGE
    Organisme org = new Organisme();
    org.setId(1);
    org.setNom("CIH Bank");

    when(organismeRepository.findById(1)).thenReturn(Optional.of(org));

    // ACT
    Optional<Organisme> result = organismeService.getOrganismeById(1L);

    // ASSERT
    assertTrue(result.isPresent());
    assertEquals("CIH Bank", result.get().getNom());
  }

  @Test
  void testGetAllOrganismes() {
    // ARRANGE
    Organisme org1 = new Organisme();
    org1.setNom("OFPPT");
    Organisme org2 = new Organisme();
    org2.setNom("CIH Bank");

    when(organismeRepository.findAll()).thenReturn(Arrays.asList(org1, org2));

    // ACT
    List<Organisme> result = organismeService.getAllOrganismes();

    // ASSERT
    assertEquals(2, result.size());
  }

  @Test
  void testDeleteOrganisme() {
    // ARRANGE
    doNothing().when(organismeRepository).deleteById(1);

    // ACT
    organismeService.deleteOrganisme(1L);

    // ASSERT → vérifier que deleteById a bien été appelé
    verify(organismeRepository, times(1)).deleteById(1);
  }

  @Test
  void testHasProjetsEnCours() {
    // ARRANGE
    Projet projet = new Projet();
    projet.setDateFin(LocalDate.now().plusMonths(3)); // projet en cours

    when(projetRepository.findByOrganismeId(1))
            .thenReturn(Arrays.asList(projet));

    // ACT
    boolean result = organismeService.hasProjetsEnCours(1L);

    // ASSERT
    assertTrue(result);
  }
}

