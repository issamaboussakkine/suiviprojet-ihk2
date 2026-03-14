package ma.projet.ihk.suiviprojet_ihk.repositories;

import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des livrables.
 * Permet de récupérer les documents produits par phase.
 */
@Repository
public interface LivrableRepository extends JpaRepository<Livrable, Integer> {

  // Récupérer tous les livrables d'une phase
  List<Livrable> findByPhaseId(int phaseId);
}