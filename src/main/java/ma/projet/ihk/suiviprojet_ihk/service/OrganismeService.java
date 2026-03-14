package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import java.util.List;
import java.util.Optional;

// Service organisme client
public interface OrganismeService {
    // Opérations de base
    Organisme saveOrganisme(Organisme organisme);
    Organisme updateOrganisme(Long id, Organisme organisme);
    void deleteOrganisme(Long id);

    // Recherches
    Optional<Organisme> getOrganismeById(Long id);
    Optional<Organisme> getOrganismeByCode(String code);
    List<Organisme> getAllOrganismes();
    List<Organisme> searchOrganismesByNom(String nom);

    // Projets liés
    List<Projet> getProjetsByOrganisme(Long organismeId);
    long countProjetsByOrganisme(Long organismeId);

    // Statistiques
    boolean hasProjetsEnCours(Long organismeId);
}