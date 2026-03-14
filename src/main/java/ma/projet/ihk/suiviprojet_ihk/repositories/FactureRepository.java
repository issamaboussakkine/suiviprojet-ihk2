package ma.projet.ihk.suiviprojet_ihk.repositories;


import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour la gestion des factures.
 * Contient les recherches par période pour le reporting comptable.
 */
@Repository
public interface FactureRepository extends JpaRepository<Facture, Integer> {

  // Rechercher une facture liée à une phase spécifique
  Facture findByPhaseId(int phaseId);

  // Rechercher les factures émises entre deux dates → reporting comptable
  List<Facture> findByDateFactureBetween(LocalDate dateDebut, LocalDate dateFin);
}