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
}