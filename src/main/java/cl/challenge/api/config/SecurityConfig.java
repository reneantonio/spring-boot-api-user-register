package cl.challenge.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF
            .authorizeHttpRequests(request -> 
                request
                    .requestMatchers("/api/register", "/api/register/**").permitAll() // Permite acceso público a /api/register
                    .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
            )
            .exceptionHandling(handling -> 
                handling
                    // Enviar un error 401 en lugar de redirigir a la página de login
                    .authenticationEntryPoint((request, response, authException) -> 
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado"))
            );  // Usa autenticación básica (puedes cambiar a JWT o tu método preferido)

        return http.build();
    }

}
