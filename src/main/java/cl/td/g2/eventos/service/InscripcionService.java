package cl.td.g2.eventos.service;

import cl.td.g2.eventos.dto.InscripcionDTO;
import cl.td.g2.eventos.exception.BadRequestException;
import cl.td.g2.eventos.exception.NotFoundException;
import cl.td.g2.eventos.mapper.InscripcionMapper;
import cl.td.g2.eventos.model.Inscripcion;
import cl.td.g2.eventos.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private InscripcionMapper inscripcionMapper;

    public List<InscripcionDTO> getAllInscripciones() {
        List<Inscripcion> inscripciones = inscripcionRepository.findAll();
        if (inscripciones == null || inscripciones.isEmpty()) {
        	throw new NotFoundException("Inscripciones");
        }
        return inscripcionMapper.toDTO(inscripciones);
    }

    public InscripcionDTO getInscripcionById(Long id) {
        return inscripcionRepository.findById(id).map(inscripcionMapper::toDTO).orElseThrow(() -> new NotFoundException("Inscripción", "id", id));
    }

    public InscripcionDTO createInscripcion(InscripcionDTO inscripcionDTO) {
        Inscripcion inscripcion = inscripcionMapper.toEntity(inscripcionDTO);
        try {
        	Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);
            return inscripcionMapper.toDTO(savedInscripcion);
		} catch (Exception ex) {
			throw new BadRequestException(ex.getMessage());
		}
    }

    public InscripcionDTO updateInscripcion(Long id, InscripcionDTO inscripcionDTO) {
        if (inscripcionRepository.existsById(id)) {
        	Inscripcion inscripcion = inscripcionMapper.toEntity(inscripcionDTO);
            inscripcion.setId(id);
            try {
            	Inscripcion updatedInscripcion = inscripcionRepository.save(inscripcion);
                return inscripcionMapper.toDTO(updatedInscripcion);
    		} catch (Exception ex) {
    			throw new BadRequestException(ex.getMessage());
    		}
        }
        throw new NotFoundException("Inscripción", "id", id);
    }

    public void deleteInscripcion(Long id) {
        if (inscripcionRepository.existsById(id)) {
    		try {
    			inscripcionRepository.deleteById(id);
    		} catch (Exception ex) {
    			throw new BadRequestException(ex.getMessage());
    		}
        }
    	else {
    		throw new NotFoundException("Inscripción", "id", id);
    	}
    }
}
