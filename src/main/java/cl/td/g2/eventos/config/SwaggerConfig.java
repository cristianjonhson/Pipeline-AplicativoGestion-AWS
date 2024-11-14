package cl.td.g2.eventos.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
	info=@Info(
		title="American Summer API",
		description="Documentación de la API del CRUD de Eventos de Verano",
		version="1.0.0"
	)
)

public class SwaggerConfig {

}
