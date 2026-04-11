package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import java.util.List;
import java.util.Optional;

// Service projet
public interface ProjetService {
    // Opérations de base
    Projet saveProjet(Projet projet);
    Projet updateProjet(Long id, Projet projet);
    void deleteProjet(Long id);

    // Recherches
    Optional<Projet> getProjetById(Long id);
    Optional<Projet> getProjetByCode(String code);
    List<Projet> getAllProjets();
    List<Projet> getProjetsByOrganisme(Long organismeId);
    List<Projet> getProjetsByChefProjet(Long chefProjetId);
    List<Projet> searchProjetsByNom(String nom);

    // Statistiques
    double getMontantTotalProjets();
    long countProjetsEnCours();
    long countProjetsTermines();

    // ========== NOUVELLES MÉTHODES À AJOUTER ==========

    // Taux d'avancement
    int getTauxAvancement(Long projetId);

    // Workflow de validation
    Projet validerProjet(Long id);
    Projet demarrerProjet(Long id);
    Projet terminerProjet(Long id);

    // Récupérer tous les projets avec leur taux
    List<Projet> getAllProjetsAvecTaux();
}