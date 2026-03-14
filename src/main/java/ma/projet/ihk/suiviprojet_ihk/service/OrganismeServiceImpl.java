package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.repositories.OrganismeRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrganismeServiceImpl implements OrganismeService {

    @Autowired
    private OrganismeRepository organismeRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Override
    public Organisme saveOrganisme(Organisme organisme) {
        return organismeRepository.save(organisme);
    }

    @Override
    public Organisme updateOrganisme(Long id, Organisme organisme) {
        if (organismeRepository.existsById(Math.toIntExact(id))) {
            organisme.setId(Math.toIntExact(id));
            return organismeRepository.save(organisme);
        }
        return null;
    }

    @Override
    public void deleteOrganisme(Long id) {
        organismeRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Optional<Organisme> getOrganismeById(Long id) {
        return organismeRepository.findById(Math.toIntExact(id));
    }

    @Override
    public Optional<Organisme> getOrganismeByCode(String code) {
        return Optional.ofNullable(organismeRepository.findByCode(code));
    }

    @Override
    public List<Organisme> getAllOrganismes() {
        return organismeRepository.findAll();
    }

    @Override
    public List<Organisme> searchOrganismesByNom(String nom) {
        return organismeRepository.findByNomContaining(nom);
    }

    @Override
    public List<Projet> getProjetsByOrganisme(Long organismeId) {
        return projetRepository.findByOrganismeId(Math.toIntExact(organismeId));
    }

    @Override
    public long countProjetsByOrganisme(Long organismeId) {
        return projetRepository.findByOrganismeId(Math.toIntExact(organismeId)).size();
    }

    @Override
    public boolean hasProjetsEnCours(Long organismeId) {
        LocalDate aujourdhui = LocalDate.now();
        return projetRepository.findByOrganismeId(Math.toIntExact(organismeId))
                .stream()
                .anyMatch(p -> p.getDateFin() != null && !p.getDateFin().isBefore(aujourdhui));
    }
}