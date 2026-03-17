package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.AffectationDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AffectationMapper {
    AffectationMapper INSTANCE = Mappers.getMapper(AffectationMapper.class);
    AffectationDTO toDto(Affectation affectation);
    Affectation toEntity(AffectationDTO affectationDTO);
    void updateEntityFromDto(AffectationDTO affectationDTO, @MappingTarget Affectation affectation);
}