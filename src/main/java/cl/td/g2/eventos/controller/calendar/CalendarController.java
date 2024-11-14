package cl.td.g2.eventos.controller.calendar;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.td.g2.eventos.dto.EventoDTO;
import cl.td.g2.eventos.service.EventoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CalendarController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private ObjectMapper objectMapper;  // Jackson para convertir a JSON

    @GetMapping("/calendario")
    public String verCalendario(Model model) throws JsonProcessingException {
        List<EventoDTO> eventos = eventoService.getAllEventos();
        model.addAttribute("eventosJson", convertirEventosAJson(eventos));
        return "calendario/view";
    }

    private String convertirEventosAJson(List<EventoDTO> eventos) throws JsonProcessingException {
        return objectMapper.writeValueAsString(eventos);  // Convierte directamente a JSON
    }
}
