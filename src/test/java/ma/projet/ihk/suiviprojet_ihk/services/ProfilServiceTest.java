package ma.projet.ihk.suiviprojet_ihk.services;

import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProfilRepository;
import ma.projet.ihk.suiviprojet_ihk.service.ProfilServiceImpl;
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
 * Tests unitaires pour ProfilServiceImpl
 * Teste la logique métier des profils sans base de données
 */
@ExtendWith(MockitoExtension.class)
public class ProfilServiceTest {

  @Mock
  private ProfilRepository profilRepository;

  @InjectMocks
  private ProfilServiceImpl profilService;

  @Test
  void testSaveProfil() {
    Profil profil = new Profil();
    profil.setCode("CP");
    profil.setLibelle("Chef de projet");

    when(profilRepository.save(profil)).thenReturn(profil);

    Profil result = profilService.saveProfil(profil);

    assertNotNull(result);
    assertEquals("CP", result.getCode());
    verify(profilRepository, times(1)).save(profil);
  }

  @Test
  void testUpdateProfil_Existe() {
    Profil profil = new Profil();
    profil.setCode("CP");
    profil.setLibelle("Chef de projet modifié");

    when(profilRepository.existsById(1)).thenReturn(true);
    when(profilRepository.save(any(Profil.class))).thenReturn(profil);

    Profil result = profilService.updateProfil(1L, profil);

    assertNotNull(result);
    assertEquals("Chef de projet modifié", result.getLibelle());
  }

  @Test
  void testUpdateProfil_NonTrouve() {
    Profil profil = new Profil();
    when(profilRepository.existsById(99)).thenReturn(false);

    Profil result = profilService.updateProfil(99L, profil);

    assertNull(result);
  }

  @Test
  void testGetProfilById() {
    Profil profil = new Profil();
    profil.setId(1);
    profil.setCode("COMPT");
    profil.setLibelle("Comptable");

    when(profilRepository.findById(1)).thenReturn(Optional.of(profil));

    Optional<Profil> result = profilService.getProfilById(1L);

    assertTrue(result.isPresent());
    assertEquals("COMPT", result.get().getCode());
  }

  @Test
  void testGetProfilByCode() {
    Profil profil = new Profil();
    profil.setCode("SEC");
    profil.setLibelle("Secrétaire");

    when(profilRepository.findByCode("SEC")).thenReturn(profil);

    Optional<Profil> result = profilService.getProfilByCode("SEC");

    assertTrue(result.isPresent());
    assertEquals("Secrétaire", result.get().getLibelle());
  }

  @Test
  void testGetAllProfils() {
    Profil p1 = new Profil();
    p1.setCode("CP");
    Profil p2 = new Profil();
    p2.setCode("COMPT");

    when(profilRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

    List<Profil> result = profilService.getAllProfils();

    assertEquals(2, result.size());
  }

  @Test
  void testDeleteProfil() {
    doNothing().when(profilRepository).deleteById(1);

    profilService.deleteProfil(1L);

    verify(profilRepository, times(1)).deleteById(1);
  }

  @Test
  void testIsCodeUnique_Vrai() {
    when(profilRepository.findByCode("NOUVEAU")).thenReturn(null);

    boolean result = profilService.isCodeUnique("NOUVEAU");

    assertTrue(result);
  }

  @Test
  void testIsCodeUnique_Faux() {
    Profil profil = new Profil();
    profil.setCode("CP");

    when(profilRepository.findByCode("CP")).thenReturn(profil);

    boolean result = profilService.isCodeUnique("CP");

    assertFalse(result);
  }

  @Test
  void testGetAllCodes() {
    Profil p1 = new Profil();
    p1.setCode("CP");
    Profil p2 = new Profil();
    p2.setCode("COMPT");
    Profil p3 = new Profil();
    p3.setCode("SEC");

    when(profilRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

    List<String> codes = profilService.getAllCodes();

    assertEquals(3, codes.size());
    assertTrue(codes.contains("CP"));
    assertTrue(codes.contains("COMPT"));
    assertTrue(codes.contains("SEC"));
  }
}