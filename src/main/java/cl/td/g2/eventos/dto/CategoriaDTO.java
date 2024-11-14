package cl.td.g2.eventos.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaDTO {

    private Long id;
    @NotBlank(message = "El nombre de la Categoría es requerido")
    private String nombre;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}