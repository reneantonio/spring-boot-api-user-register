package cl.challenge.api.controllers;

import cl.challenge.api.models.User;
import cl.challenge.api.models.UserRequest;
import cl.challenge.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint público para registrar un usuario
    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario y lo devuelve con sus ids y token")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRequest userRequest) {
        try {
            User user = userService.registerUser(userRequest);
            // return ResponseEntity.status(HttpStatus.CREATED).body(user);
             // Autenticar al usuario automáticamente después del registro
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"" + e.getMessage() + "\"}");
        }
    }
}
