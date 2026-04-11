package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PhaseServiceImpl implements PhaseService {

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Override
    public Phase savePhase(Phase phase) {
        return phaseRepository.save(phase);
    }

    @Override
    public Phase updatePhase(Long id, Phase phase) {
        if (phaseRepository.existsById(Math.toIntExact(id))) {
            phase.setId(Math.toIntExact(id));
            return phaseRepository.save(phase);
        }
        return null;
    }

    @Override
    public void deletePhase(Long id) {
        phaseRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Optional<Phase> getPhaseById(Long id) {
        return phaseRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<Phase> getAllPhases() {
        return phaseRepository.findAll();
    }

    @Override
    public List<Phase> getPhasesByProjet(Long projetId) {
        return phaseRepository.findByProjetId(Math.toIntExact(projetId));
    }

    @Override
    public List<Phase> getPhasesTermineesNonFacturees() {
        return phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse();
    }

    @Override
    public List<Phase> getPhasesFactureesNonPayees() {
        return phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse();
    }

    @Override
    public List<Phase> getPhasesPayees() {
        return phaseRepository.findByEtatPaiementTrue();
    }

    @Override
    public List<Phase> getPhasesTermineesByProjet(Long projetId) {
        return phaseRepository.findByProjetIdAndEtatRealisationTrue(Math.toIntExact(projetId));
    }

    @Override
    public Phase marquerTerminee(Long id) {
        Optional<Phase> phaseOpt = phaseRepository.findById(Math.toIntExact(id));
        if (phaseOpt.isPresent()) {
            Phase phase = phaseOpt.get();
            phase.setEtatRealisation(true);
            return phaseRepository.save(phase);
        }
        return null;
    }

    @Override
    public Phase marquerFacturee(Long id) {
        Optional<Phase> phaseOpt = phaseRepository.findById(Math.toIntExact(id));
        if (phaseOpt.isPresent()) {
            Phase phase = phaseOpt.get();
            phase.setEtatFacturation(true);
            return phaseRepository.save(phase);
        }
        return null;
    }

    @Override
    public Phase marquerPayee(Long id) {
        Optional<Phase> phaseOpt = phaseRepository.findById(Math.toIntExact(id));
        if (phaseOpt.isPresent()) {
            Phase phase = phaseOpt.get();
            phase.setEtatPaiement(true);
            return phaseRepository.save(phase);
        }
        return null;
    }

    // ========== NOUVELLES MÉTHODES WORKFLOW ==========

    @Override
    public Phase demarrerPhase(Long id) {
        Optional<Phase> phaseOpt = phaseRepository.findById(Math.toIntExact(id));
        if (phaseOpt.isPresent()) {
            Phase phase = phaseOpt.get();
            phase.demarrer();
            return phaseRepository.save(phase);
        }
        throw new RuntimeException("Phase non trouvée avec l'ID: " + id);
    }

    @Override
    public Phase terminerPhase(Long id) {
        Optional<Phase> phaseOpt = phaseRepository.findById(Math.toIntExact(id));
        if (phaseOpt.isPresent()) {
            Phase phase = phaseOpt.get();
            phase.terminer();
            return phaseRepository.save(phase);
        }
        throw new RuntimeException("Phase non trouvée avec l'ID: " + id);
    }

    @Override
    public double getMontantTotalPhasesByProjet(Long projetId) {
        return phaseRepository.findByProjetId(Math.toIntExact(projetId))
                .stream()
                .mapToDouble(Phase::getMontant)
                .sum();
    }

    @Override
    public long countPhasesEnCoursByProjet(Long projetId) {
        LocalDate aujourdhui = LocalDate.now();
        return phaseRepository.findByProjetId(Math.toIntExact(projetId))
                .stream()
                .filter(p -> !p.isEtatRealisation())
                .filter(p -> p.getDateFin() == null || !p.getDateFin().isBefore(aujourdhui))
                .count();
    }
}