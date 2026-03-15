package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.DocumentDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Document;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);
    DocumentDTO toDto(Document document);
    Document toEntity(DocumentDTO documentDTO);
    void updateEntityFromDto(DocumentDTO documentDTO, @MappingTarget Document document);
}