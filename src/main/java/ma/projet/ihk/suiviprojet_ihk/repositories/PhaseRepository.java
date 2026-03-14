package ma.projet.ihk.suiviprojet_ihk.repositories;


import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des phases de projet.
 * Contient les requêtes métier pour le suivi et la facturation.
 */
@Repository
public interface PhaseRepository extends JpaRepository<Phase, Integer> {

  // Récupérer toutes les phases d'un projet
  List<Phase> findByProjetId(int projetId);

  // Phases terminées mais pas encore facturées → utile pour le comptable
  List<Phase> findByEtatRealisationTrueAndEtatFacturationFalse();

  // Phases facturées mais pas encore payées → utile pour le comptable
  List<Phase> findByEtatFacturationTrueAndEtatPaiementFalse();

  // Phases entièrement payées
  List<Phase> findByEtatPaiementTrue();

  // Phases terminées d'un projet spécifique
  List<Phase> findByProjetIdAndEtatRealisationTrue(int projetId);
}