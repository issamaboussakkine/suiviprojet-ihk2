package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import java.util.List;
import java.util.Optional;

// Service phase
public interface PhaseService {
    // Opérations de base
    Phase savePhase(Phase phase);
    Phase updatePhase(Long id, Phase phase);
    void deletePhase(Long id);

    // Recherches
    Optional<Phase> getPhaseById(Long id);
    List<Phase> getAllPhases();
    List<Phase> getPhasesByProjet(Long projetId);
    List<Phase> getPhasesTermineesNonFacturees();
    List<Phase> getPhasesFactureesNonPayees();
    List<Phase> getPhasesPayees();
    List<Phase> getPhasesTermineesByProjet(Long projetId);

    // Mise à jour des états
    Phase marquerTerminee(Long id);
    Phase marquerFacturee(Long id);
    Phase marquerPayee(Long id);

    // Statistiques
    double getMontantTotalPhasesByProjet(Long projetId);
    long countPhasesEnCoursByProjet(Long projetId);
}