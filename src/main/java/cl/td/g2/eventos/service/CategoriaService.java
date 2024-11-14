package cl.td.g2.eventos.service;

import cl.td.g2.eventos.dto.CategoriaDTO;
import cl.td.g2.eventos.exception.BadRequestException;
import cl.td.g2.eventos.exception.NotFoundException;
import cl.td.g2.eventos.mapper.CategoriaMapper;
import cl.td.g2.eventos.model.Categoria;
import cl.td.g2.eventos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    public List<CategoriaDTO> getAllCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias == null || categorias.isEmpty()) {
        	throw new NotFoundException("Categorías");
        }
        return categoriaMapper.toDTO(categorias);
    }

    public CategoriaDTO getCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Categoría", "id", id));
    }

    public CategoriaDTO createCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        try {
            Categoria savedCategoria = categoriaRepository.save(categoria);
            return categoriaMapper.toDTO(savedCategoria);
		} catch (Exception ex) {
			throw new BadRequestException(ex.getMessage());
		}
    }

    public CategoriaDTO updateCategoria(Long id, CategoriaDTO categoriaDTO) {
        // Buscar la entidad existente directamente
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría", "id", id));

        // Mapear los cambios desde el DTO a la entidad existente
        categoriaMapper.updateEntityFromDTO(categoriaDTO, categoria);

        try {
            // Guardar la entidad actualizada
            Categoria updatedCategoria = categoriaRepository.save(categoria);
            return categoriaMapper.toDTO(updatedCategoria);
        } catch (Exception ex) {
            throw new BadRequestException("Error actualizando la categoría: " + ex.getMessage());
        }
    }

    public void deleteCategoria(Long id) {
        if (categoriaRepository.existsById(id)) {
    		try {
    			categoriaRepository.deleteById(id);
    		} catch (Exception ex) {
    			throw new BadRequestException(ex.getMessage());
    		}
        }
    	else {
    		throw new NotFoundException("Categoría", "id", id);
    	}
    }
}