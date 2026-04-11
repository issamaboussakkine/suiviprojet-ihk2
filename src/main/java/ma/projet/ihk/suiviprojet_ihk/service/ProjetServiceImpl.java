package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetServiceImpl implements ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Override
    public Projet saveProjet(Projet projet) {
        return projetRepository.save(projet);
    }

    @Override
    public Projet updateProjet(Long id, Projet projet) {
        if (projetRepository.existsById(Math.toIntExact(id))) {
            projet.setId(Math.toIntExact(id));
            return projetRepository.save(projet);
        }
        return null;
    }

    @Override
    public void deleteProjet(Long id) {
        projetRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Optional<Projet> getProjetById(Long id) {
        return projetRepository.findById(Math.toIntExact(id));
    }

    @Override
    public Optional<Projet> getProjetByCode(String code) {
        return Optional.ofNullable(projetRepository.findByCode(code));
    }

    @Override
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    @Override
    public List<Projet> getProjetsByOrganisme(Long organismeId) {
        return projetRepository.findByOrganismeId(Math.toIntExact(organismeId));
    }

    @Override
    public List<Projet> getProjetsByChefProjet(Long chefProjetId) {
        return projetRepository.findByChefProjetId(Math.toIntExact(chefProjetId));
    }

    @Override
    public List<Projet> searchProjetsByNom(String nom) {
        return projetRepository.findByNomContaining(nom);
    }

    @Override
    public double getMontantTotalProjets() {
        return projetRepository.findAll()
                .stream()
                .mapToDouble(Projet::getMontant)
                .sum();
    }

    @Override
    public long countProjetsEnCours() {
        LocalDate aujourdhui = LocalDate.now();
        return projetRepository.findAll()
                .stream()
                .filter(p -> p.getDateDebut() != null && p.getDateFin() != null)
                .filter(p -> !p.getDateFin().isBefore(aujourdhui))
                .count();
    }

    @Override
    public long countProjetsTermines() {
        LocalDate aujourdhui = LocalDate.now();
        return projetRepository.findAll()
                .stream()
                .filter(p -> p.getDateFin() != null && p.getDateFin().isBefore(aujourdhui))
                .count();
    }

    // ========== NOUVELLES MÉTHODES ==========

    @Override
    public int getTauxAvancement(Long projetId) {
        Optional<Projet> projetOpt = getProjetById(projetId);
        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();
            if (projet.getPhases() == null || projet.getPhases().isEmpty()) {
                return 0;
            }
            long totalPhases = projet.getPhases().size();
            long phasesTerminees = projet.getPhases().stream()
                    .filter(phase -> phase.isEtatRealisation())
                    .count();
            return (int) (phasesTerminees * 100 / totalPhases);
        }
        return 0;
    }

    @Override
    public List<Projet> getAllProjetsAvecTaux() {
        List<Projet> projets = getAllProjets();
        for (Projet projet : projets) {
            // Forcer le calcul du taux
            projet.getTauxAvancement();
        }
        return projets;
    }

    @Override
    public Projet validerProjet(Long id) {
        Optional<Projet> projetOpt = getProjetById(id);
        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();
            if (projet.getStatut() == null || "EN_ATTENTE".equals(projet.getStatut())) {
                projet.setStatut("VALIDE");
                return updateProjet(id, projet);
            } else {
                throw new RuntimeException("Le projet ne peut pas être validé (statut actuel: " + projet.getStatut() + ")");
            }
        }
        throw new RuntimeException("Projet non trouvé avec l'ID: " + id);
    }

    @Override
    public Projet demarrerProjet(Long id) {
        Optional<Projet> projetOpt = getProjetById(id);
        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();
            if ("VALIDE".equals(projet.getStatut())) {
                projet.setStatut("EN_COURS");
                return updateProjet(id, projet);
            } else {
                throw new RuntimeException("Le projet ne peut pas démarrer (statut actuel: " + projet.getStatut() + ")");
            }
        }
        throw new RuntimeException("Projet non trouvé avec l'ID: " + id);
    }

    @Override
    public Projet terminerProjet(Long id) {
        Optional<Projet> projetOpt = getProjetById(id);
        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();
            if ("EN_COURS".equals(projet.getStatut())) {
                projet.setStatut("TERMINE");
                return updateProjet(id, projet);
            } else {
                throw new RuntimeException("Seul un projet en cours peut être terminé (statut actuel: " + projet.getStatut() + ")");
            }
        }
        throw new RuntimeException("Projet non trouvé avec l'ID: " + id);
    }
}