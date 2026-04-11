package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.AffectationDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Affectation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AffectationMapper {

    AffectationMapper INSTANCE = Mappers.getMapper(AffectationMapper.class);

    @Mapping(target = "employeNom", source = "employe.nom")
    @Mapping(target = "employePrenom", source = "employe.prenom")
    @Mapping(target = "phaseLibelle", source = "phase.libelle")
    @Mapping(target = "projetNom", source = "phase.projet.nom")
    @Mapping(target = "projetId", source = "phase.projet.id")
    AffectationDTO toDto(Affectation affectation);

    Affectation toEntity(AffectationDTO affectationDTO);
}