package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import java.util.List;
import java.util.Optional;

// Service profil fonctionnel
public interface ProfilService {
    // Opérations de base
    Profil saveProfil(Profil profil);
    Profil updateProfil(Long id, Profil profil);
    void deleteProfil(Long id);

    // Recherches
    Optional<Profil> getProfilById(Long id);
    Optional<Profil> getProfilByCode(String code);
    List<Profil> getAllProfils();

    // Utilitaires
    boolean isCodeUnique(String code);
    List<String> getAllCodes();
}