package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.ProfilDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Profil;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfilMapper {
    ProfilMapper INSTANCE = Mappers.getMapper(ProfilMapper.class);
    ProfilDTO toDto(Profil profil);
    Profil toEntity(ProfilDTO profilDTO);
    void updateEntityFromDto(ProfilDTO profilDTO, @MappingTarget Profil profil);
}