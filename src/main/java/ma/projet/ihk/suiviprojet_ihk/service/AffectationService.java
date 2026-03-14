package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Service affectation employé-phase
public interface AffectationService {
    // Opérations de base
    Affectation saveAffectation(Affectation affectation);
    Affectation updateAffectation(AffectationId id, Affectation affectation);
    void deleteAffectation(AffectationId id);

    // Recherches
    Optional<Affectation> getAffectationById(AffectationId id);
    List<Affectation> getAllAffectations();
    List<Affectation> getAffectationsByPhase(Long phaseId);
    List<Affectation> getAffectationsByEmploye(Long employeId);

    // Vérifications
    boolean isEmployeAffectePhase(Long employeId, Long phaseId);
    boolean isEmployeDisponible(Long employeId, LocalDate dateDebut, LocalDate dateFin);

    // Statistiques
    long countEmployesByPhase(Long phaseId);
    long countPhasesByEmploye(Long employeId);
}