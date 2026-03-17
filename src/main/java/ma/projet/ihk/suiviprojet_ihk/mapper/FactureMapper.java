package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.FactureDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Facture;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FactureMapper {
    FactureMapper INSTANCE = Mappers.getMapper(FactureMapper.class);
    FactureDTO toDto(Facture facture);
    Facture toEntity(FactureDTO factureDTO);
    void updateEntityFromDto(FactureDTO factureDTO, @MappingTarget Facture facture);
}