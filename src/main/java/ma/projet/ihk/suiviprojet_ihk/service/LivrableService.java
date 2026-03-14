package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import java.util.List;
import java.util.Optional;

// Service livrable
public interface LivrableService {
    // Opérations de base
    Livrable saveLivrable(Livrable livrable);
    Livrable updateLivrable(Long id, Livrable livrable);
    void deleteLivrable(Long id);

    // Recherches
    Optional<Livrable> getLivrableById(Long id);
    List<Livrable> getAllLivrables();
    List<Livrable> getLivrablesByPhase(Long phaseId);

    // Statistiques
    long countLivrablesByPhase(Long phaseId);
    boolean hasLivrables(Long phaseId);
}