package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.LivrableDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Livrable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LivrableMapper {
    LivrableMapper INSTANCE = Mappers.getMapper(LivrableMapper.class);
    LivrableDTO toDto(Livrable livrable);
    Livrable toEntity(LivrableDTO livrableDTO);
    void updateEntityFromDto(LivrableDTO livrableDTO, @MappingTarget Livrable livrable);
}