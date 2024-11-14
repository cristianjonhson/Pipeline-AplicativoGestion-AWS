package cl.td.g2.eventos.dto;


import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;

public class InscripcionDTO {

    private Long id;
    @NotNull(message = "El usuario es requerido")
    private Long usuarioId;
    @NotNull(message = "El evento es requerido")
    private Long eventoId;
    @NotNull(message = "La fecha de inscripción es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaInscripcion;
    
    public InscripcionDTO(InscripcionDTO inscripcion) {
		this.id = inscripcion.id;
		this.usuarioId = inscripcion.usuarioId;
		this.eventoId = inscripcion.eventoId;
		this.fechaInscripcion = inscripcion.fechaInscripcion;
	}
    
	public InscripcionDTO() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
	public Long getEventoId() {
		return eventoId;
	}
	public void setEventoId(Long eventoId) {
		this.eventoId = eventoId;
	}
	public LocalDateTime getFechaInscripcion() {
		return fechaInscripcion;
	}
	public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

}