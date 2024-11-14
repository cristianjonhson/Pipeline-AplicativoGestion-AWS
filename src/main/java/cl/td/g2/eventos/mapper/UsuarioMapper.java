package cl.td.g2.eventos.mapper;

import cl.td.g2.eventos.dto.UsuarioDTO;
import cl.td.g2.eventos.model.Usuario;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO usuarioDTO);
	List<UsuarioDTO> toDTO(List<Usuario> usuarios);
}
