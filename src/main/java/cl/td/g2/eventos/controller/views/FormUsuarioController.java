package cl.td.g2.eventos.controller.views;

import cl.td.g2.eventos.dto.UsuarioDTO;
import cl.td.g2.eventos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class FormUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar el formulario para registrar un nuevo usuario
    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioUsuario(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "user/form"; // Plantilla en templates/user/form.html
    }

    // Guardar un nuevo usuario
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute @Validated UsuarioDTO usuarioDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/form";
        }
        try {
            usuarioDTO.setFechaRegistro(LocalDateTime.now());
            // No se codifica la contraseña
            usuarioService.createUsuario(usuarioDTO);
            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente.");
            return "redirect:/usuarios/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al registrar el usuario.");
            return "user/form";
        }
    }

    // Mostrar el formulario de edición para un usuario existente
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEdicionUsuario(@PathVariable("id") Long id, Model model) {
        UsuarioDTO usuarios = usuarioService.getUsuarioById(id);
        if (usuarios == null) {
            return "redirect:/error"; // Redirigir a una página de error si no se encuentra el usuarios
        }
        model.addAttribute("usuario", usuarios);
        return "user/edit"; // Plantilla en templates/user/edit.html
    }

    // Actualizar un usuario existente
    @PostMapping("/usuarios/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
            @Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/edit"; // Volver a mostrar el formulario si hay errores
        }
        try {
            // No se codifica la contraseña en la actualización
            usuarioService.updateUsuario(id, usuarioDTO);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente.");
            return "redirect:/usuarios/lista"; // Redirigir al listado después de la actualización
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al actualizar el usuario.");
            return "user/edit";
        }
    }

    // Eliminar un usuario
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteUsuario(id);
            redirectAttributes.addFlashAttribute("deleted", "Usuario eliminado exitosamente.");
            return "redirect:/usuarios/lista"; // Redirigir al listado de usuarios
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al eliminar el usuario.");
            return "redirect:/usuarios/lista";
        }
    }

    // Listar todos los usuarios
    @GetMapping("/usuarios/lista")
    public String listarUsuarios(Model model) {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios(); // Obtener todos los usuarios
        model.addAttribute("usuarios", usuarios);
        return "user/list"; // Plantilla en templates/user/list.html
    }
}
