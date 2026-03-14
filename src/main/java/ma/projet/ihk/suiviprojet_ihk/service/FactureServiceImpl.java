package ma.projet.ihk.suiviprojet_ihk.service;

import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import ma.projet.ihk.suiviprojet_ihk.repositories.FactureRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FactureServiceImpl implements FactureService {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Override
    public Facture saveFacture(Facture facture) {
        // Vérifier que la phase n'est pas déjà facturée
        if (facture.getPhase() != null) {
            Facture existante = factureRepository.findByPhaseId(facture.getPhase().getId());
            if (existante != null) {
                throw new RuntimeException("Cette phase a déjà une facture");
            }
        }
        return factureRepository.save(facture);
    }

    @Override
    public Facture updateFacture(Long id, Facture facture) {
        if (factureRepository.existsById(Math.toIntExact(id))) {
            facture.setId(Math.toIntExact(id));
            return factureRepository.save(facture);
        }
        return null;
    }

    @Override
    public void deleteFacture(Long id) {
        factureRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Optional<Facture> getFactureById(Long id) {
        return factureRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    @Override
    public Optional<Facture> getFactureByPhase(Long phaseId) {
        return Optional.ofNullable(factureRepository.findByPhaseId(Math.toIntExact(phaseId)));
    }

    @Override
    public List<Facture> getFacturesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return factureRepository.findByDateFactureBetween(dateDebut, dateFin);
    }

    @Override
    public boolean phaseDejaFacturee(Long phaseId) {
        return factureRepository.findByPhaseId(Math.toIntExact(phaseId)) != null;
    }

    @Override
    public boolean factureExiste(Long id) {
        return factureRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public long countFacturesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return factureRepository.findByDateFactureBetween(dateDebut, dateFin).size();
    }
}