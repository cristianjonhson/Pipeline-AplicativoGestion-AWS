package cl.td.g2.eventos.dto;

public class EventoListDTO extends EventoDTO {
	private String organizador;
    private String categoria;
    private String ciudad;
    
    public EventoListDTO(EventoDTO eventoDTO, String organizador, String categoria, String ciudad) {
    	super(eventoDTO);
    	this.organizador = organizador;
    	this.categoria = categoria;
    	this.ciudad = ciudad;
    }

	public String getOrganizador() {
		return organizador;
	}
	public void setOrganizador(String organizador) {
		this.organizador = organizador;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
}
