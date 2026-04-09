package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.repositories.AffectationRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.EmployeRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AffectationServiceImpl implements AffectationService {

    @Autowired
    private AffectationRepository affectationRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Override
    public Affectation saveAffectation(Affectation affectation) {
        // Récupérer l'employé et la phase à partir des IDs
        if (affectation.getId() != null) {
            Optional<Employe> employeOpt = employeRepository.findById(affectation.getId().getEmployeId());
            if (employeOpt.isPresent()) {
                affectation.setEmploye(employeOpt.get());
            }

            Optional<Phase> phaseOpt = phaseRepository.findById(affectation.getId().getPhaseId());
            if (phaseOpt.isPresent()) {
                affectation.setPhase(phaseOpt.get());
            }

            // Vérifier que l'employé est disponible sur la période
            if (!isEmployeDisponible(
                    Long.valueOf(affectation.getId().getEmployeId()),
                    affectation.getDateDebut(),
                    affectation.getDateFin())) {
                throw new RuntimeException("L'employé n'est pas disponible sur cette période");
            }
        }
        return affectationRepository.save(affectation);
    }

    @Override
    public Affectation updateAffectation(AffectationId id, Affectation affectation) {
        if (affectationRepository.existsById(id)) {
            affectation.setId(id);
            return affectationRepository.save(affectation);
        }
        return null;
    }

    @Override
    public void deleteAffectation(AffectationId id) {
        affectationRepository.deleteById(id);
    }

    @Override
    public Optional<Affectation> getAffectationById(AffectationId id) {
        return affectationRepository.findById(id);
    }

    @Override
    public List<Affectation> getAllAffectations() {
        return affectationRepository.findAll();
    }

    @Override
    public List<Affectation> getAffectationsByPhase(Long phaseId) {
        return affectationRepository.findByPhaseId(Math.toIntExact(phaseId));
    }

    @Override
    public List<Affectation> getAffectationsByEmploye(Long employeId) {
        return affectationRepository.findByEmployeId(Math.toIntExact(employeId));
    }

    @Override
    public boolean isEmployeAffectePhase(Long employeId, Long phaseId) {
        AffectationId id = new AffectationId(employeId.intValue(), phaseId.intValue());
        return affectationRepository.existsById(id);
    }

    @Override
    public boolean isEmployeDisponible(Long employeId, LocalDate dateDebut, LocalDate dateFin) {
        List<Affectation> affectations = affectationRepository.findByEmployeId(Math.toIntExact(employeId));

        for (Affectation affectation : affectations) {
            LocalDate debutExistant = affectation.getDateDebut();
            LocalDate finExistant = affectation.getDateFin();

            if (debutExistant != null && finExistant != null) {
                if (!(dateFin.isBefore(debutExistant) || dateDebut.isAfter(finExistant))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public long countEmployesByPhase(Long phaseId) {
        return affectationRepository.findByPhaseId(Math.toIntExact(phaseId)).size();
    }

    @Override
    public long countPhasesByEmploye(Long employeId) {
        return affectationRepository.findByEmployeId(Math.toIntExact(employeId)).size();
    }
}