package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.AffectationDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import ma.projet.ihk.suiviprojet_ihk.repositories.EmployeRepository;
import ma.projet.ihk.suiviprojet_ihk.repositories.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AffectationMapper {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    public AffectationDTO toDto(Affectation affectation) {
        if (affectation == null) return null;

        AffectationDTO dto = new AffectationDTO();
        if (affectation.getId() != null) {
            dto.setEmployeId(affectation.getId().getEmployeId());
            dto.setPhaseId(affectation.getId().getPhaseId());
        }
        dto.setDateDebut(affectation.getDateDebut());
        dto.setDateFin(affectation.getDateFin());

        // Ajouter le nom de l'employé
        if (affectation.getEmploye() != null) {
            dto.setEmployeNom(affectation.getEmploye().getNom());
        }

        // Ajouter le libellé de la phase et le nom du projet
        if (affectation.getPhase() != null) {
            dto.setPhaseLibelle(affectation.getPhase().getLibelle());
            if (affectation.getPhase().getProjet() != null) {
                dto.setProjetNom(affectation.getPhase().getProjet().getNom());
            }
        }

        return dto;
    }

    public Affectation toEntity(AffectationDTO dto) {
        if (dto == null) return null;

        Affectation affectation = new Affectation();
        affectation.setId(new AffectationId(dto.getEmployeId(), dto.getPhaseId()));
        affectation.setDateDebut(dto.getDateDebut());
        affectation.setDateFin(dto.getDateFin());

        // Récupérer et associer l'employé
        employeRepository.findById(dto.getEmployeId()).ifPresent(affectation::setEmploye);

        // Récupérer et associer la phase
        phaseRepository.findById(dto.getPhaseId()).ifPresent(affectation::setPhase);

        return affectation;
    }

    public void updateEntityFromDto(AffectationDTO dto, Affectation affectation) {
        if (dto == null || affectation == null) return;

        affectation.setDateDebut(dto.getDateDebut());
        affectation.setDateFin(dto.getDateFin());
    }
}