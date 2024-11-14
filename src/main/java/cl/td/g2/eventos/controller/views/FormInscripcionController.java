package cl.td.g2.eventos.controller.views;


import cl.td.g2.eventos.dto.EventoDTO;
import cl.td.g2.eventos.dto.InscripcionDTO;
import cl.td.g2.eventos.dto.InscripcionListDTO;
import cl.td.g2.eventos.dto.UsuarioDTO;
import cl.td.g2.eventos.service.EventoService;
import cl.td.g2.eventos.service.InscripcionService;
import cl.td.g2.eventos.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FormInscripcionController {

    @Autowired
    private InscripcionService inscripcionService;
    
    @Autowired
	private UsuarioService usuarioService;
    
    @Autowired
	private EventoService eventoService;

    // Mostrar el formulario de inscripción
    @GetMapping("/inscripciones/nueva")
    public String mostrarFormularioInscripcion(Model model) {
        model.addAttribute("inscripcionDTO", new InscripcionDTO());
        model.addAttribute("usuarios", usuarioService.getAllUsuarios());
        model.addAttribute("eventos", eventoService.getAllEventos());
        return "inscription/form"; // Asegúrate de que la plantilla esté en templates/inscription/form.html
    }

    // Guardar la inscripción cuando se envía el formulario
    @PostMapping("/inscripciones/guardar")
    public String guardarInscripcion(@ModelAttribute @Validated InscripcionDTO inscripcionDTO,
                                     BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "inscription/form"; // Volver al formulario si hay errores
        }
        try {
            inscripcionService.createInscripcion(inscripcionDTO);
            redirectAttributes.addFlashAttribute("success", "Inscripción guardada exitosamente.");
            return "redirect:/inscripciones/lista"; // Redirigir después de guardar
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al guardar la inscripción.");
            return "inscription/form"; // Si hay error, volver a mostrar el formulario
        }
    }

    // Mostrar el formulario de edición de una inscripción existente
    @GetMapping("/inscripciones/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        InscripcionDTO inscripcion = inscripcionService.getInscripcionById(id);
        if (inscripcion == null) {
            return "redirect:/error"; // Redirige a una página de error si no se encuentra la inscripción
        }
        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("usuarios", usuarioService.getAllUsuarios());
        model.addAttribute("eventos", eventoService.getAllEventos());
        return "inscription/edit"; // Asegúrate de tener un formulario de edición en templates/inscription/edit.html
    }

    // Actualizar una inscripción existente
    @PostMapping("/inscripciones/editar/{id}")
    public String editarInscripcion(@PathVariable Long id,
                                    @Valid @ModelAttribute("inscripcion") InscripcionDTO inscripcionDTO,
                                    BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "inscription/edit"; // Volver al formulario si hay errores
        }

        try {
            inscripcionService.updateInscripcion(id, inscripcionDTO);
            redirectAttributes.addFlashAttribute("success", "Inscripción actualizada exitosamente.");
            return "redirect:/inscripciones/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al actualizar la inscripción.");
            return "inscription/edit";
        }
    }

    // Eliminar una inscripción
    @GetMapping("/inscripciones/eliminar/{id}")
    public String eliminarInscripcion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inscripcionService.deleteInscripcion(id);
            redirectAttributes.addFlashAttribute("deleted", true);
            return "redirect:/inscripciones/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al eliminar la inscripción.");
            return "redirect:/inscripciones/lista";
        }
    }

    // Listar todas las inscripciones
    @GetMapping("/inscripciones/lista")
    public String listarInscripciones(Model model) {
    	List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios();
    	List<EventoDTO> eventos = eventoService.getAllEventos();
        List<InscripcionDTO> inscripciones = inscripcionService.getAllInscripciones(); // Obtener todas las inscripciones
        List<InscripcionListDTO> inscripcionList = inscripciones.stream().map(inscripcion -> {
			UsuarioDTO usuario = usuarios.stream().filter(usr -> usr.getId().equals(inscripcion.getUsuarioId())).findFirst().orElse(new UsuarioDTO());
			String participante = (usuario.getNombre() != null ? usuario.getNombre() : "") + " " +
					(usuario.getApellido() != null ? usuario.getApellido() : "");
			String tituloEvento = eventos.stream().filter(evento -> inscripcion.getEventoId() == evento.getId()).map(EventoDTO::getTitulo).findFirst().orElse("");
			return new InscripcionListDTO(inscripcion, participante.trim(), tituloEvento);
		}).collect(Collectors.toList());
        model.addAttribute("inscripcionList", inscripcionList);
        return "inscription/list"; // Nombre de la plantilla que lista las inscripciones en templates/inscription/list.html
    }
}
