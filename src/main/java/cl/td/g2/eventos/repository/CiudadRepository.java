package cl.td.g2.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.td.g2.eventos.model.Ciudad;

import java.util.List;
import java.util.Optional;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    
    // Encontrar por ID
    Optional<Ciudad> findById(Long id);

    // Encontrar todos
    List<Ciudad> findAll();

    // Guardar
    @SuppressWarnings("unchecked")
	Ciudad save(Ciudad ciudad);

    // Eliminar por ID
    void deleteById(Long id);
    
    // Método para encontrar una ciudad por nombre
    Optional<Ciudad> findByNombre(String nombre);

    // Método para verificar si una ciudad con el nombre especificado existe
    boolean existsByNombre(String nombre);
}
