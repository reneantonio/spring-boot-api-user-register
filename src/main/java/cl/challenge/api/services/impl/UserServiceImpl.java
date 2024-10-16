package cl.challenge.api.services.impl;

import cl.challenge.api.models.User;
import cl.challenge.api.models.UserRequest;
import cl.challenge.api.repositories.UserRepository;
import cl.challenge.api.services.UserService;
import cl.challenge.api.exceptions.EmailAlreadyExistsException;
import cl.challenge.api.exceptions.InvalidEmailFormatException;
import cl.challenge.api.exceptions.InvalidPasswordException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    // Cargar las expresiones regulares desde application.properties
    @Value("${validation.email.regex}")
    private String emailRegex;

    @Value("${validation.password.regex}")
    private String passwordRegex;

    // Cargar los mensajes configurables desde application.properties
    @Value("${error.email.already.exists}")
    private String emailAlreadyExistsMessage;

    @Value("${error.email.invalid.format}")
    private String invalidEmailFormatMessage;

    @Value("${error.password.invalid}")
    private String invalidPasswordMessage;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserRequest userRequest) {
        
        // Validar si el correo ya está registrado
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyExistsException(emailAlreadyExistsMessage);
        }

        // Validar el formato del correo con una expresión regular
        if (!Pattern.matches(emailRegex, userRequest.getEmail())) {
            throw new InvalidEmailFormatException(invalidEmailFormatMessage);
        }

        // Validar la contraseña con una expresión regular (configurable)
        if (!Pattern.matches(passwordRegex, userRequest.getPassword())) {
            throw new InvalidPasswordException(invalidPasswordMessage);
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setPhones(userRequest.getPhones());
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setToken(UUID.randomUUID().toString());
        user.setActive(true);

        return userRepository.save(user);
    }

}