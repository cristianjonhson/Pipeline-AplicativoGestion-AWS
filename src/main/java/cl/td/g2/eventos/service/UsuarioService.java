package cl.td.g2.eventos.service;

import cl.td.g2.eventos.dto.UsuarioDTO;
import cl.td.g2.eventos.exception.BadRequestException;
import cl.td.g2.eventos.exception.NotFoundException;
import cl.td.g2.eventos.mapper.UsuarioMapper;
import cl.td.g2.eventos.model.Usuario;
import cl.td.g2.eventos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    // Obtener todos los usuarios
    public List<UsuarioDTO> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios == null || usuarios.isEmpty()) {
            throw new NotFoundException("Usuarios");
        }
        return usuarioMapper.toDTO(usuarios);
    }

    // Obtener un usuario por su ID
    public UsuarioDTO getUsuarioById(Long id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Usuario", "id", id));
    }

    // Crear un nuevo usuario
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        // No se codifica la contraseña
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        try {
            Usuario savedUsuario = usuarioRepository.save(usuario);
            return usuarioMapper.toDTO(savedUsuario);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    // Actualizar un usuario existente
    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsById(id)) {
            // No se codifica la contraseña
            Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
            usuario.setId(id);
            try {
                Usuario updatedUsuario = usuarioRepository.save(usuario);
                return usuarioMapper.toDTO(updatedUsuario);
            } catch (Exception ex) {
                throw new BadRequestException(ex.getMessage());
            }
        }
        throw new NotFoundException("Usuario", "id", id);
    }

    // Eliminar un usuario
    public void deleteUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            try {
                usuarioRepository.deleteById(id);
            } catch (Exception ex) {
                throw new BadRequestException(ex.getMessage());
            }
        } else {
            throw new NotFoundException("Usuario", "id", id);
        }
    }

    // Validar el inicio de sesión del usuario
    public boolean loginUsuario(String email, String contrasena) {
        Optional<Usuario> usuarioByEmail = usuarioRepository.findByEmail(email);
        if (usuarioByEmail.isPresent()) {
            // Comparar contraseñas en texto plano
            return contrasena.equals(usuarioByEmail.get().getContrasena());
        }
        return false;
    }

    // Buscar un usuario por su email
    public UsuarioDTO findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> usuarioMapper.toDTO(usuario))
                .orElse(null);
    }
}
