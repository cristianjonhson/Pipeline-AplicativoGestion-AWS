package cl.td.g2.eventos.controller.views;

import cl.td.g2.eventos.dto.CategoriaDTO;
import cl.td.g2.eventos.dto.CiudadDTO;
import cl.td.g2.eventos.dto.EventoDTO;
import cl.td.g2.eventos.dto.EventoListDTO;
import cl.td.g2.eventos.dto.UsuarioDTO;
import cl.td.g2.eventos.service.CategoriaService;
import cl.td.g2.eventos.service.CiudadService;
import cl.td.g2.eventos.service.EventoService;
import cl.td.g2.eventos.service.UsuarioService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FormEventoController {

	@Autowired
	private EventoService eventoService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private UsuarioService usuarioService;

	// Mostrar el formulario para crear un nuevo evento
	@GetMapping("/evento/nuevo")
	public String mostrarFormularioEvento(Model model) {
		model.addAttribute("eventoDTO", new EventoDTO());
		model.addAttribute("categorias", categoriaService.getAllCategorias());
		model.addAttribute("ciudades", ciudadService.getAllCiudades());
		model.addAttribute("usuarios", usuarioService.getAllUsuarios());
		return "event/form";
	}

	// Guardar el evento y redirigir al listado
	@PostMapping("/evento/guardar")
	public String guardarEvento(@ModelAttribute @Validated EventoDTO eventoDTO,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "event/form"; // Volver al formulario si hay errores
		}

		try {
			eventoService.createEvento(eventoDTO);
			// Agregar un atributo para señalar que fue exitoso
			redirectAttributes.addFlashAttribute("success", true);
			return "redirect:/evento/lista"; // Volver al formulario con el mensaje de éxito
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al guardar el Evento.");
			return "event/form"; // Si hay error, se queda en el formulario
		}
	}

	// Mostrar el formulario de edición de un evento existente
	@GetMapping("/evento/editar/{id}")
	public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
		EventoDTO evento = eventoService.getEventoById(id);
		if (evento == null) {
			return "redirect:/error"; // O cualquier página de error que prefieras
		}
		model.addAttribute("evento", evento);
		model.addAttribute("categorias", categoriaService.getAllCategorias());
		model.addAttribute("ciudades", ciudadService.getAllCiudades());
		model.addAttribute("usuarios", usuarioService.getAllUsuarios());
		return "event/edit";
	}

	// Actualizar un evento existente
	@PostMapping("/evento/editar/{id}")
	public String editarEvento(@PathVariable Long id, @Valid @ModelAttribute("evento") EventoDTO eventoDTO,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "event/edit"; // Volver a mostrar el formulario si hay errores
		}

		eventoService.updateEvento(id, eventoDTO);
		redirectAttributes.addAttribute("success", true);
		return "redirect:/evento/editar/{id}";
	}

	// Eliminar un evento y redirigir al listado
	@GetMapping("/evento/eliminar/{id}")
	public String eliminarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			eventoService.deleteEvento(id);
			// Agregar el atributo de éxito y redirigir a la lista de eventos
			redirectAttributes.addAttribute("deleted", true);
			return "redirect:/evento/lista";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al eliminar el Evento.");
			return "redirect:/evento/lista?error=true"; // Redirige con un mensaje de error
		}
	}

	// Listar todos los Eventos
	@GetMapping("/evento/lista")
	public String listarEventos(Model model) {
		List<CategoriaDTO> categorias = categoriaService.getAllCategorias();
		List<CiudadDTO> ciudades = ciudadService.getAllCiudades();
		List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios();
		List<EventoDTO> eventos = eventoService.getAllEventos();

		List<EventoListDTO> eventoList = eventos.stream().map(evento -> {
			// Buscar el nombre de la categoría
			String nombreCategoria = categorias.stream()
					.filter(categoria -> categoria.getId().equals(evento.getCategoriaId()))
					.map(CategoriaDTO::getNombre)
					.findFirst()
					.orElse("");

			// Buscar el organizador
			UsuarioDTO usuario = usuarios.stream()
					.filter(usr -> usr.getId().equals(evento.getOrganizadorId()))
					.findFirst()
					.orElse(new UsuarioDTO());
			String nombreOrganizador = (usuario.getNombre() != null ? usuario.getNombre() : "") + " " +
					(usuario.getApellido() != null ? usuario.getApellido() : "");

			// Buscar el nombre de la ciudad
			String nombreCiudad = ciudades.stream()
					.filter(ciudad -> ciudad.getId().equals(evento.getCiudadId()))
					.map(CiudadDTO::getNombre)
					.findFirst()
					.orElse("");

			// Crear el DTO para la lista de eventos
			return new EventoListDTO(evento, nombreOrganizador.trim(), nombreCategoria, nombreCiudad);
		}).collect(Collectors.toList());

		model.addAttribute("eventoList", eventoList);
		return "event/list"; // Nombre de la plantilla que lista los eventos
	}

}
