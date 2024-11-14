package cl.td.g2.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.td.g2.eventos.model.Categoria;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Encontrar por ID
    Optional<Categoria> findById(Long id);

    // Encontrar todos
    List<Categoria> findAll();

    // Guardar
    @SuppressWarnings("unchecked")
	Categoria save(Categoria categoria);

    // Eliminar por ID
    void deleteById(Long id);
    
    // Método para encontrar una categoría por nombre
    Optional<Categoria> findByNombre(String nombre);

    // Método para verificar si una categoría con el nombre especificado existe
    boolean existsByNombre(String nombre);
}
