package ma.projet.ihk.suiviprojet_ihk.mapper;

import ma.projet.ihk.suiviprojet_ihk.dto.EmployeDTO;
import ma.projet.ihk.suiviprojet_ihk.entities.Employe;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeMapper {
    EmployeMapper INSTANCE = Mappers.getMapper(EmployeMapper.class);
    EmployeDTO toDto(Employe employe);
    Employe toEntity(EmployeDTO employeDTO);
    void updateEntityFromDto(EmployeDTO employeDTO, @MappingTarget Employe employe);
}