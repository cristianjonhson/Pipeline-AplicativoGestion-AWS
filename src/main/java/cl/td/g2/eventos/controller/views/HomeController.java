package cl.td.g2.eventos.controller.views;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import cl.td.g2.eventos.model.Usuario;

@Controller
public class HomeController {

     @GetMapping("/home")
    public String getHomePage(Model model) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuario = (Usuario) authentication.getPrincipal(); // Asegúrate de que Usuario sea el tipo correcto
            model.addAttribute("usuario", usuario);
        }

        return "layout/home"; // Thymeleaf buscará en src/main/resources/templates/layout/home.html
    }

    @GetMapping("/about")
    public String getAboutPage() {
    	return "layout/about"; // Correcto, Thymeleaf buscará en src/main/resources/templates/layout/about.html
    }

    @GetMapping("/terms")
    public String getTermsPage() {
    	return "layout/terms"; // Correcto, Thymeleaf buscará en src/main/resources/templates/layout/terms.html
    }

    @GetMapping("/privacy")
    public String getPrivacyPage() {
    	return "layout/privacy"; // Correcto, Thymeleaf buscará en src/main/resources/templates/layout/privacy.html
    }

    @GetMapping
    public String getDefaultPage() {
    	return "layout/home";
    }

    @GetMapping("/contact")
    public String showContactForm() {
        return "layout/contact"; // Retorna la vista contact.html
    }

    @PostMapping("/contact")
    public String handleContactForm(@RequestParam String nombre, 
                                     @RequestParam String email, 
                                     @RequestParam String mensaje) {
        // Lógica para manejar el envío del formulario
        // Por ejemplo, enviar un correo o guardar en la base de datos

        return "redirect:/home"; // Redirige después de enviar
    }

   
}
