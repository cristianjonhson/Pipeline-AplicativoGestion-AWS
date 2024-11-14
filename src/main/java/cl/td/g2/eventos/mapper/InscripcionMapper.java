package cl.td.g2.eventos.mapper;

import cl.td.g2.eventos.dto.InscripcionDTO;
import cl.td.g2.eventos.model.Inscripcion;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EventoMapper.class, UsuarioMapper.class})
public interface InscripcionMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "evento.id", target = "eventoId")
    InscripcionDTO toDTO(Inscripcion inscripcion);

    @Mapping(source = "usuarioId", target = "usuario.id")
    @Mapping(source = "eventoId", target = "evento.id")
    Inscripcion toEntity(InscripcionDTO inscripcionDTO);

    List<InscripcionDTO> toDTO(List<Inscripcion> inscripciones);

    // Este método actualiza la entidad existente con los valores del DTO
    @Mapping(target = "id", ignore = true)  // Ignoramos el ID para no sobreescribirlo accidentalmente
    @Mapping(target = "usuario.id", source = "usuarioId")
    @Mapping(target = "evento.id", source = "eventoId")
    void updateEntityFromDTO(InscripcionDTO inscripcionDTO, @MappingTarget Inscripcion inscripcion);
}
