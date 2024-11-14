package cl.td.g2.eventos.mapper;

import cl.td.g2.eventos.dto.CiudadDTO;
import cl.td.g2.eventos.model.Ciudad;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CiudadMapper {
    CiudadDTO toDTO(Ciudad ciudad);
    Ciudad toEntity(CiudadDTO ciudadDTO);
	List<CiudadDTO> toDTO(List<Ciudad> ciudades);
    // Este método actualiza la entidad existente con los valores del DTO
    @Mapping(target = "id", ignore = true)  // Ignoramos el ID para no sobreescribirlo accidentalmente
    void updateEntityFromDTO(CiudadDTO ciudadDTO, @MappingTarget Ciudad ciudad);
}
