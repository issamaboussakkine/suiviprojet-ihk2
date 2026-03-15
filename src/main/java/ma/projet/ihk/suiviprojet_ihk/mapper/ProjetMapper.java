package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.ProjetDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjetMapper {
    ProjetMapper INSTANCE = Mappers.getMapper(ProjetMapper.class);
    ProjetDTO toDto(Projet projet);
    Projet toEntity(ProjetDTO projetDTO);
    void updateEntityFromDto(ProjetDTO projetDTO, @MappingTarget Projet projet);
}