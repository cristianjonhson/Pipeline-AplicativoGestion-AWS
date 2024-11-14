package cl.td.g2.eventos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/login").permitAll()  // Rutas públicas
                .requestMatchers("/calendario", "/inscripcion").hasRole("usuario") // Cambiar a "usuario" si lo renombraste a "ROLE_usuario"
                .anyRequest().authenticated()  // Todas las demás rutas requieren autenticación
            .and()
            .formLogin()
                .loginPage("/login")  // Página de login personalizada
                .defaultSuccessUrl("/")  // Página después de iniciar sesión exitosamente
                .permitAll()
            .and()
            .logout()
                .permitAll();
        return http.build();
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
