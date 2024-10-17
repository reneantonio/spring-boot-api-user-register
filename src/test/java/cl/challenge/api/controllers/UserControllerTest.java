package cl.challenge.api.controllers;

import cl.challenge.api.models.User;
import cl.challenge.api.models.UserRequest;
import cl.challenge.api.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequest mockUserRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build(); // Configura MockMvc para pruebas del controlador

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
    void shouldRegisterUserSuccessfully() throws Exception {
        // Simular la respuesta del servicio
        when(userService.registerUser(any(UserRequest.class))).thenReturn(mockUser);

        // JSON de la solicitud
        String userJson = "{\"name\":\"Test User\", \"email\":\"test@gmail.com\", \"password\":\"Password1\"}";

        // Ejecutar el POST y verificar la respuesta
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated()) // Verifica que el estado sea 201 Created
                .andExpect(jsonPath("$.email", is("test@gmail.com"))) // Verifica que el campo email sea el correcto
                .andExpect(jsonPath("$.name", is("Test User"))); // Verifica que el nombre sea correcto
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        // Simulamos un correo con formato incorrecto en la solicitud
        String invalidUserJson = "{\"name\":\"Test User\", \"email\":\"invalid-email\", \"password\":\"Password1\"}";

        // Ejecutar el POST y verificar que el estado sea 400 Bad Request
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }
}
