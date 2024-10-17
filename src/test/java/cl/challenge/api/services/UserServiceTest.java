package cl.challenge.api.services;

import cl.challenge.api.models.User;
import cl.challenge.api.models.UserRequest;
import cl.challenge.api.repositories.UserRepository;
import cl.challenge.api.services.impl.UserServiceImpl;
import cl.challenge.api.config.JwtUtil;
import cl.challenge.api.exceptions.EmailAlreadyExistsException;
import cl.challenge.api.exceptions.InvalidEmailFormatException;
import cl.challenge.api.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest mockUserRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inyectar manualmente los valores de las propiedades @Value
        ReflectionTestUtils.setField(userService, "emailRegex", "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        ReflectionTestUtils.setField(userService, "passwordRegex", "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}");
        ReflectionTestUtils.setField(userService, "emailAlreadyExistsMessage", "El correo ya está registrado");
        ReflectionTestUtils.setField(userService, "invalidEmailFormatMessage", "El formato del correo no es válido");
        ReflectionTestUtils.setField(userService, "invalidPasswordMessage", "La contraseña no cumple con los requisitos");

        mockUserRequest = new UserRequest();
        mockUserRequest.setEmail("test@gmail.com");
        mockUserRequest.setName("Test User");
        mockUserRequest.setPassword("Password1");

        mockUser = new User();
        mockUser.setEmail("test@gmail.com");
        mockUser.setName("Test User");
        mockUser.setPassword("Password1");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(jwtUtil.generateToken(anyString())).thenReturn("mocked-jwt-token");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User savedUser = userService.registerUser(mockUserRequest);

        assertNotNull(savedUser);
        assertEquals("test@gmail.com", savedUser.getEmail());
        // assertEquals("mocked-jwt-token", savedUser.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.registerUser(mockUserRequest);
        });

        String expectedMessage = "El correo ya está registrado";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenInvalidEmailFormat() {
        mockUserRequest.setEmail("invalid-email-format");

        Exception exception = assertThrows(InvalidEmailFormatException.class, () -> {
            userService.registerUser(mockUserRequest);
        });

        String expectedMessage = "El formato del correo no es válido";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenInvalidPassword() {
        mockUserRequest.setPassword("123");

        Exception exception = assertThrows(InvalidPasswordException.class, () -> {
            userService.registerUser(mockUserRequest);
        });

        String expectedMessage = "La contraseña no cumple con los requisitos";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(userRepository, times(0)).save(any(User.class));
    }
}
