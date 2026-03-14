package ma.projet.ihk.suiviprojet_ihk.repositories;

import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des profils fonctionnels.
 * Fournit les opérations CRUD de base automatiquement.
 */
@Repository
public interface ProfilRepository extends JpaRepository<Profil, Integer> {

  // Rechercher un profil par son code unique (ex: "CP", "COMPT")
  Profil findByCode(String code);
}