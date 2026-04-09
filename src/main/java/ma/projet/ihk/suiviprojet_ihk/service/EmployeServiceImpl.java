package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import ma.projet.ihk.suiviprojet_ihk.repositories.EmployeRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProfilRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.AffectationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;  // ← AJOUTER CETTE LIGNE

    @Override
    public Employe saveEmploye(Employe employe) {
        if (employe.getPassword() != null && !employe.getPassword().startsWith("$2a")) {
            employe.setPassword(passwordEncoder.encode(employe.getPassword()));
        }
        return employeRepository.save(employe);
    }

    @Override
    public Employe updateEmploye(Long id, Employe employe) {
        if (employeRepository.existsById(Math.toIntExact(id))) {
            employe.setId(Math.toIntExact(id));
            if (employe.getPassword() != null && !employe.getPassword().startsWith("$2a")) {
                employe.setPassword(passwordEncoder.encode(employe.getPassword()));
            }
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
        return employeRepository.findByProfilId(Math.toIntExact(profilId));
    }

    @Override
    public List<Employe> searchEmployesByNom(String nom) {
        return employeRepository.findAll()
                .stream()
                .filter(e -> e.getNom() != null && e.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Employe authentifier(String login, String password) {
        System.out.println("=== TENTATIVE DE CONNEXION ===");
        System.out.println("Login reçu: " + login);
        System.out.println("Password reçu: " + password);

        Employe employe = employeRepository.findByLogin(login);

        if (employe == null) {
            System.out.println("ERREUR: Aucun employé trouvé avec login: " + login);
            return null;
        }

        System.out.println("Employé trouvé: " + employe.getLogin());
        System.out.println("Hash en base: " + employe.getPassword());

        boolean matches = passwordEncoder.matches(password, employe.getPassword());
        System.out.println("Password matches: " + matches);

        if (matches) {
            System.out.println("Authentification réussie !");
            return employe;
        } else {
            System.out.println("ERREUR: Mot de passe incorrect");
            return null;
        }
    }

    @Override
    public boolean isEmployeDisponible(Long employeId, Date dateDebut, Date dateFin) {
        return true;
    }
}