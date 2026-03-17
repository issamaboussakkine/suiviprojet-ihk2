package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.PhaseDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Phase;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PhaseMapper {
    PhaseMapper INSTANCE = Mappers.getMapper(PhaseMapper.class);
    PhaseDTO toDto(Phase phase);
    Phase toEntity(PhaseDTO phaseDTO);
    void updateEntityFromDto(PhaseDTO phaseDTO, @MappingTarget Phase phase);
}