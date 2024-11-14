package cl.td.g2.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.td.g2.eventos.model.Evento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    // Encontrar por ID
    Optional<Evento> findById(Long id);

    // Encontrar todos
    List<Evento> findAll();

    // Guardar
    @SuppressWarnings("unchecked")
	Evento save(Evento evento);

    // Eliminar por ID
    void deleteById(Long id);
    
    // Método para encontrar eventos por título
    List<Evento> findByTitulo(String titulo);
    
    // Método para encontrar eventos por ubicación
    List<Evento> findByUbicacion(String ubicacion);

    // Método para encontrar eventos por organizador
    List<Evento> findByOrganizadorId(Long organizadorId);

    // Método para encontrar eventos por categoría
    List<Evento> findByCategoriaId(Long categoriaId);

    // Método para encontrar eventos por ciudad
    List<Evento> findByCiudadId(Long ciudadId);

    // Método para encontrar eventos que están activos (fecha de inicio en el futuro)
    List<Evento> findByFechaInicioAfter(LocalDateTime fechaInicio);

    // Método para encontrar eventos que han finalizado (fecha de fin en el pasado)
    List<Evento> findByFechaFinBefore(LocalDateTime fechaFin);
}
