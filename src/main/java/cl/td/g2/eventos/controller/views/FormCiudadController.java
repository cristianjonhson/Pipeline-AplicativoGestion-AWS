package cl.td.g2.eventos.controller.views;

import cl.td.g2.eventos.dto.CiudadDTO;
import cl.td.g2.eventos.service.CiudadService; // Asegúrate de tener este servicio
import jakarta.validation.Valid;

import java.util.List;

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
public class FormCiudadController {

    @Autowired
    private CiudadService ciudadService;

    // Mostrar el formulario para crear una nueva ciudad
    @GetMapping("/ciudades/nuevo")
    public String mostrarFormularioCiudades(Model model) {
        model.addAttribute("CiudadDTO", new CiudadDTO());
        return "city/form";
    }

    // Guardar la ciudad y redirigir al listado
    @PostMapping("/ciudades/guardar")
    public String guardarCiudad(@ModelAttribute @Validated CiudadDTO ciudadDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "city/form"; // Volver al formulario si hay errores
        }
        try {
            ciudadService.createCiudad(ciudadDTO);
            // Agregar un atributo para señalar que fue exitoso
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/ciudades/lista"; // Volver al formulario con el mensaje de éxito
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al guardar la ciudad.");
            return "city/form"; // Si hay error, se queda en el formulario
        }
    }

    // Mostrar el formulario de edición de una ciudad existente
    @GetMapping("/ciudad/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        CiudadDTO ciudad = ciudadService.getCiudadById(id);
        if (ciudad == null) {
            return "redirect:/error"; // O cualquier página de error que prefieras
        }
        model.addAttribute("ciudad", ciudad);
        return "city/edit";
    }

    // Actualizar una ciudad existente
    @PostMapping("/ciudad/editar/{id}")
    public String editarCiudad(
            @PathVariable Long id,
            @Valid @ModelAttribute("ciudad") CiudadDTO ciudadDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "city/edit"; // Volver a mostrar el formulario si hay errores
        }

        ciudadService.updateCiudad(id, ciudadDTO);
        redirectAttributes.addAttribute("success", true);
        return "redirect:/ciudad/editar/{id}";
    }

     // Eliminar una ciudad y redirigir al listado
    @GetMapping("/ciudad/eliminar/{id}")
    public String eliminarCiudad(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ciudadService.deleteCiudad(id);
            // Agregar el atributo de éxito y redirigir a la lista de ciudades
            redirectAttributes.addFlashAttribute("deleted", true);
            return "redirect:/ciudades/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al eliminar la ciudad.");
            return "redirect:/ciudades?error=true"; // Redirige con un mensaje de error
        }
    }

    // Listar todas las ciudades
    @GetMapping("/ciudades/lista")
    public String listarCiudades(Model model) {
        List<CiudadDTO> ciudades = ciudadService.getAllCiudades(); // Obtener todas las ciudades
        model.addAttribute("ciudades", ciudades);
        return "city/list"; // Nombre de la plantilla que lista las ciudades
    }
}