package co.edu.udea.salasinfo.controller.v1;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.auth.AuthenticationRequest;
import co.edu.udea.salasinfo.dto.request.auth.RegisterRequest;
import co.edu.udea.salasinfo.dto.response.auth.AuthenticationResponse;
import co.edu.udea.salasinfo.dto.response.auth.RegisterResponse;
import co.edu.udea.salasinfo.exceptions.EmailAlreadyRegisteredException;
import co.edu.udea.salasinfo.service.AuthenticationService;
import co.edu.udea.salasinfo.utils.enums.RoleName;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    JwtService jwtService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password123");
        AuthenticationResponse response = new AuthenticationResponse("token123", RoleName.USER, "id");

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("token123"))
                .andExpect(jsonPath("$.role").value(RoleName.USER.name()));
    }

    @Test
    void testLogin_BadRequest() throws Exception {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("", ""); // Invalid input

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123");
        RegisterResponse response = new RegisterResponse("User registered successfully");

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void testRegister_BadRequest() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest(null, null, null, null); // Invalid input

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_Conflict() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123");

        when(authenticationService.register(any(RegisterRequest.class))).thenThrow(new EmailAlreadyRegisteredException(request.getEmail()));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}