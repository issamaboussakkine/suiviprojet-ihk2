package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.FactureDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FactureMapper {
    FactureMapper INSTANCE = Mappers.getMapper(FactureMapper.class);

    @Mapping(target = "phaseLibelle", source = "phase.libelle")
    @Mapping(target = "phaseId", source = "phase.id")
    @Mapping(target = "projetNom", source = "phase.projet.nom")
    @Mapping(target = "montant", source = "phase.montant")
    FactureDTO toDto(Facture facture);

    Facture toEntity(FactureDTO factureDTO);

    void updateEntityFromDto(FactureDTO factureDTO, @MappingTarget Facture facture);
}