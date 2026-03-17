package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.OrganismeDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Organisme;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganismeMapper {

    OrganismeMapper INSTANCE = Mappers.getMapper(OrganismeMapper.class);

    OrganismeDTO toDto(Organisme organisme);

    Organisme toEntity(OrganismeDTO organismeDTO);

    void updateEntityFromDto(OrganismeDTO organismeDTO, @MappingTarget Organisme organisme);
}