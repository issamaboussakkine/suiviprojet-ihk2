package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.AffectationDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import ma.projet.ihk.suiviprojet_ihk.entities.AffectationId;
import org.springframework.stereotype.Component;

@Component
public class AffectationMapper {

    public AffectationDTO toDto(Affectation affectation) {
        if (affectation == null) return null;

        AffectationDTO dto = new AffectationDTO();
        if (affectation.getId() != null) {
            dto.setEmployeId(affectation.getId().getEmployeId());
            dto.setPhaseId(affectation.getId().getPhaseId());
        }
        dto.setDateDebut(affectation.getDateDebut());
        dto.setDateFin(affectation.getDateFin());
        return dto;
    }

    public Affectation toEntity(AffectationDTO dto) {
        if (dto == null) return null;

        Affectation affectation = new Affectation();
        affectation.setId(new AffectationId(dto.getEmployeId(), dto.getPhaseId()));
        affectation.setDateDebut(dto.getDateDebut());
        affectation.setDateFin(dto.getDateFin());
        return affectation;
    }

    public void updateEntityFromDto(AffectationDTO dto, Affectation affectation) {
        if (dto == null || affectation == null) return;

        affectation.setDateDebut(dto.getDateDebut());
        affectation.setDateFin(dto.getDateFin());
    }
}