package cl.td.g2.eventos.mapper;

import cl.td.g2.eventos.dto.CategoriaDTO;
import cl.td.g2.eventos.model.Categoria;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaDTO toDTO(Categoria categoria);
    Categoria toEntity(CategoriaDTO categoriaDTO);
	List<CategoriaDTO> toDTO(List<Categoria> categorias);
    // Este método actualiza la entidad existente con los valores del DTO
    @Mapping(target = "id", ignore = true)  // Ignoramos el ID para no sobreescribirlo accidentalmente
    void updateEntityFromDTO(CategoriaDTO categoriaDTO, @MappingTarget Categoria categoria);
}
