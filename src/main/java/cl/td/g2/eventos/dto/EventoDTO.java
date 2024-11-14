package cl.td.g2.eventos.dto;


import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class EventoDTO {

	private Long id;
	@NotBlank(message = "El título del Evento es requerido")
    private String titulo;
    private String descripcion;
    @NotNull(message = "La fechaInicio no debe ser nula")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaInicio;
    @NotNull(message = "La fechaFin no debe ser nula")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaFin;
    private String ubicacion;
    private Long organizadorId;
    private Long categoriaId;
    private Long ciudadId;
    private BigDecimal valor;
    private String imagenHtml;
    @NotNull(message = "La fechaCreacion no debe ser nula")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaCreacion;
	
    public EventoDTO() {
		this.fechaCreacion = LocalDateTime.now();
	}

	public EventoDTO(EventoDTO evento) {
		this.id = evento.id;
		this.titulo = evento.titulo;
		this.descripcion = evento.descripcion;
		this.fechaInicio = evento.fechaInicio;
		this.fechaFin = evento.fechaFin;
		this.ubicacion = evento.ubicacion;
		this.organizadorId = evento.organizadorId;
		this.categoriaId = evento.categoriaId;
		this.ciudadId = evento.ciudadId;
		this.valor = evento.valor;
		this.imagenHtml = evento.imagenHtml;
		this.fechaCreacion = evento.fechaCreacion;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public LocalDateTime getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public Long getOrganizadorId() {
		return organizadorId;
	}
	public void setOrganizadorId(Long organizadorId) {
		this.organizadorId = organizadorId;
	}
	public Long getCategoriaId() {
		return categoriaId;
	}
	public void setCategoriaId(Long categoriaId) {
		this.categoriaId = categoriaId;
	}
	public Long getCiudadId() {
		return ciudadId;
	}
	public void setCiudadId(Long ciudadId) {
		this.ciudadId = ciudadId;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public String getImagenHtml() {
		return imagenHtml;
	}
	public void setImagenHtml(String imagenHtml) {
		this.imagenHtml = imagenHtml;
	}
	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}