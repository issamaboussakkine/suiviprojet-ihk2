package ma.projet.ihk.suiviprojet_ihk.repositories;

import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository central pour la gestion des projets.
 * Contient les recherches métier principales du système.
 */
@Repository
public interface ProjetRepository extends JpaRepository<Projet, Integer> {

  // Rechercher un projet par son code unique
  Projet findByCode(String code);

  // Rechercher tous les projets d'un organisme client
  List<Projet> findByOrganismeId(int organismeId);

  // Rechercher tous les projets gérés par un chef de projet
  List<Projet> findByChefProjetId(int chefProjetId);

  // Rechercher les projets dont le nom contient un mot clé
  List<Projet> findByNomContaining(String nom);
}