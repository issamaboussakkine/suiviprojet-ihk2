package ma.projet.ihk.suiviprojet_ihk.repositories;


import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des affectations employé-phase.
 * Utilise la clé composée AffectationId comme identifiant.
 */
@Repository
public interface AffectationRepository extends JpaRepository<Affectation, AffectationId> {

  // Récupérer toutes les affectations d'une phase
  List<Affectation> findByPhaseId(int phaseId);

  // Récupérer toutes les phases sur lesquelles un employé intervient
  List<Affectation> findByEmployeId(int employeId);
}