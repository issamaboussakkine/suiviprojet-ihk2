package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Service facture
public interface FactureService {
    // Opérations de base
    Facture saveFacture(Facture facture);
    Facture updateFacture(Long id, Facture facture);
    void deleteFacture(Long id);

    // Recherches
    Optional<Facture> getFactureById(Long id);
    List<Facture> getAllFactures();
    Optional<Facture> getFactureByPhase(Long phaseId);
    List<Facture> getFacturesByPeriode(LocalDate dateDebut, LocalDate dateFin);

    // Vérifications
    boolean phaseDejaFacturee(Long phaseId);
    boolean factureExiste(Long id);

    // Statistiques
    long countFacturesByPeriode(LocalDate dateDebut, LocalDate dateFin);
}