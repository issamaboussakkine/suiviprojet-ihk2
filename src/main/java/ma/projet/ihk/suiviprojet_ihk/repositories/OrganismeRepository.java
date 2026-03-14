package ma.projet.ihk.suiviprojet_ihk.repositories;

import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des organismes clients.
 * Permet la recherche multi-critères sur les clients.
 */
@Repository
public interface OrganismeRepository extends JpaRepository<Organisme, Integer> {

  // Rechercher un organisme par son code unique
  Organisme findByCode(String code);

  // Rechercher les organismes dont le nom contient un mot clé
  List<Organisme> findByNomContaining(String nom);
}