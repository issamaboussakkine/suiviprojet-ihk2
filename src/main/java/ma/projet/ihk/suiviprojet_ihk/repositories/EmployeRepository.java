package ma.projet.ihk.suiviprojet_ihk.repositories;


import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des employés.
 * Contient les recherches métier utiles pour l'affectation et la sécurité.
 */
@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {

  // Rechercher un employé par son matricule RH unique
  Employe findByMatricule(String matricule);

  // Rechercher un employé par son login (utilisé pour l'authentification)
  Employe findByLogin(String login);

  // Rechercher un employé par son email
  Employe findByEmail(String email);

  // Rechercher tous les employés d'un profil donné
  List<Employe> findByProfilId(int profilId);
}