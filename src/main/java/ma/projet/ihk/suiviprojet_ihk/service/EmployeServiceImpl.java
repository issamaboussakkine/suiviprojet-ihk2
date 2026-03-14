package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import ma.projet.ihk.suiviprojet_ihk.repositories.EmployeRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProfilRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.AffectationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class EmployeServiceImpl implements EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private AffectationRepository affectationRepository;

    @Override
    public Employe saveEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    @Override
    public Employe updateEmploye(Long id, Employe employe) {
        if (employeRepository.existsById(Math.toIntExact(id))) {
            employe.setId(Math.toIntExact(id));
            return employeRepository.save(employe);
        }
        return null;
    }

    @Override
    public void deleteEmploye(Long id) {
        employeRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Optional<Employe> getEmployeById(Long id) {
        return employeRepository.findById(Math.toIntExact(id));
    }

    @Override
    public Optional<Employe> getEmployeByMatricule(String matricule) {
        return Optional.ofNullable(employeRepository.findByMatricule(matricule));
    }

    @Override
    public Optional<Employe> getEmployeByEmail(String email) {
        return Optional.ofNullable(employeRepository.findByEmail(email));
    }

    @Override
    public Optional<Employe> getEmployeByLogin(String login) {
        return Optional.ofNullable(employeRepository.findByLogin(login));
    }

    @Override
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    @Override
    public List<Employe> getEmployesByProfil(Long profilId) {
        // CORRIGÉ : Utilise findByProfilId du repository
        return employeRepository.findByProfilId(Math.toIntExact(profilId));
    }

    @Override
    public List<Employe> searchEmployesByNom(String nom) {
        // Pas de méthode findByNom dans le repository, on garde la solution avec stream
        return employeRepository.findAll()
                .stream()
                .filter(e -> e.getNom() != null && e.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Employe authentifier(String login, String password) {
        Employe employe = employeRepository.findByLogin(login);
        if (employe != null && employe.getPassword().equals(password)) {
            return employe;
        }
        return null;
    }

    @Override
    public boolean isEmployeDisponible(Long employeId, Date dateDebut, Date dateFin) {
        // Cette méthode nécessite une requête spécifique dans AffectationRepository
        // Pour l'instant, on retourne true (à améliorer plus tard)
        return true;
    }
}