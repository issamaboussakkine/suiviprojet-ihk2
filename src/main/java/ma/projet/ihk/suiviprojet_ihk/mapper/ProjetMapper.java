package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.ProjetDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Projet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjetMapper {
    ProjetMapper INSTANCE = Mappers.getMapper(ProjetMapper.class);

    @Mapping(source = "organisme.id",    target = "organismeId")
    @Mapping(source = "organisme.nom",   target = "organismeNom")
    @Mapping(source = "chefProjet.id",   target = "chefProjetId")
    @Mapping(source = "chefProjet.nom",  target = "chefProjetNom")
    @Mapping(source = "tauxAvancement",  target = "tauxAvancement")
    @Mapping(target = "nombrePhases",    expression = "java(projet.getPhases() != null ? projet.getPhases().size() : 0)")
    ProjetDTO toDto(Projet projet);

    @Mapping(target = "organisme",   ignore = true)
    @Mapping(target = "chefProjet",  ignore = true)
    @Mapping(target = "phases",      ignore = true)
    @Mapping(target = "documents",   ignore = true)
    Projet toEntity(ProjetDTO projetDTO);

    @Mapping(target = "organisme",   ignore = true)
    @Mapping(target = "chefProjet",  ignore = true)
    @Mapping(target = "phases",      ignore = true)
    @Mapping(target = "documents",   ignore = true)
    void updateEntityFromDto(ProjetDTO projetDTO, @MappingTarget Projet projet);
}