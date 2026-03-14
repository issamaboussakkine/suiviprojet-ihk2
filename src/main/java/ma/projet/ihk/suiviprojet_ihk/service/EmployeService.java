package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import java.util.List;
import java.util.Optional;

// Service employé
public interface EmployeService {
    // Opérations de base
    Employe saveEmploye(Employe employe);
    Employe updateEmploye(Long id, Employe employe);
    void deleteEmploye(Long id);

    // Recherches
    Optional<Employe> getEmployeById(Long id);
    Optional<Employe> getEmployeByMatricule(String matricule);
    Optional<Employe> getEmployeByEmail(String email);
    Optional<Employe> getEmployeByLogin(String login);
    List<Employe> getAllEmployes();
    List<Employe> getEmployesByProfil(Long profilId);
    List<Employe> searchEmployesByNom(String nom);

    // Authentification
    Employe authentifier(String login, String password);

    // Disponibilité
    boolean isEmployeDisponible(Long employeId, java.util.Date dateDebut, java.util.Date dateFin);
}