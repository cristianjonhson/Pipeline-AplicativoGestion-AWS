package cl.td.g2.eventos.dto;

public class InscripcionListDTO extends InscripcionDTO {
	private String participante;
    private String tituloEvento;
    
    public InscripcionListDTO(InscripcionDTO inscripcionDTO, String participante, String tituloEvento) {
    	super(inscripcionDTO);
    	this.participante = participante;
    	this.tituloEvento = tituloEvento;
    }
    
	public String getParticipante() {
		return participante;
	}
	public void setParticipante(String participante) {
		this.participante = participante;
	}
	public String getTituloEvento() {
		return tituloEvento;
	}
	public void setTituloEvento(String tituloEvento) {
		this.tituloEvento = tituloEvento;
	}
}
