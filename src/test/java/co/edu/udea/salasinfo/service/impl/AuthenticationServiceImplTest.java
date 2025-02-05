package co.edu.udea.salasinfo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.auth.AuthenticationRequest;
import co.edu.udea.salasinfo.dto.request.auth.RegisterRequest;
import co.edu.udea.salasinfo.dto.response.auth.AuthenticationResponse;
import co.edu.udea.salasinfo.dto.response.auth.RegisterResponse;
import co.edu.udea.salasinfo.exceptions.EmailAlreadyRegisteredException;
import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.mapper.request.RegisterRequestMapper;
import co.edu.udea.salasinfo.model.Role;
import co.edu.udea.salasinfo.model.User;
import co.edu.udea.salasinfo.persistence.RoleDAO;
import co.edu.udea.salasinfo.repository.UserRepository;
import co.edu.udea.salasinfo.utils.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RegisterRequestMapper registerRequestMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleDAO roleDAO;

    private User mockUser;
    private RoleName mockRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockRole = RoleName.Usuario;
        Role userRole = new Role(1L, mockRole);

        mockUser = User.builder()
                .id("123")
                .firstname("John")
                .lastname("Doe")
                .email("johndoe@example.com")
                .password("encoded_password")
                .role(userRole)
                .build();
    }

    @Test
    void authenticate_success() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("johndoe@example.com")
                .password("password")
                .build();

        // Mock finding the user by email
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));

        // Mock authenticationManager authenticate (no exception means success)
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        // Mock JWT token generation
        String token = "jwt_token";
        when(jwtService.generateToken(anyMap(), eq(mockUser))).thenReturn(token);

        AuthenticationResponse response = authenticationService.authenticate(request);

        // Verify the response
        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(mockRole, response.getRole());

        // Verify that authenticationManager.authenticate was called
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }



    @Test
    void authenticate_userNotFound() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("notfound@example.com")
                .password("password")
                .build();

        // Mock user not found
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                authenticationService.authenticate(request)
        );

        assertEquals("notfound@example.com", thrown.getMessage());
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void register_success() {
        RegisterRequest request = RegisterRequest.builder()
                .firstname("Jane")
                .lastname("Doe")
                .email("janedoe@example.com")
                .password("password")
                .build();

        // Mock that the email does not already exist
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        // Mock mapping from RegisterRequest to User
        when(registerRequestMapper.toEntity(request)).thenReturn(mockUser);

        // Mock role retrieval
        Role role = new Role(1L, RoleName.Usuario);
        when(roleDAO.findByRoleName(RoleName.Usuario)).thenReturn(role);

        // Mock password encoding
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_password");

        // Mock saving the user
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        RegisterResponse response = authenticationService.register(request);

        // Verify that user was saved
        verify(userRepository, times(1)).save(mockUser);

        // Assert that the response contains the correct message
        assertEquals("John Doe with 'johndoe@example.com' has been registered", response.getMessage());
    }

    @Test
    void register_emailAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder()
                .firstname("Jane")
                .lastname("Doe")
                .email("janedoe@example.com")
                .password("password")
                .build();

        // Mock that the email already exists
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        EmailAlreadyRegisteredException thrown = assertThrows(EmailAlreadyRegisteredException.class, () ->
                authenticationService.register(request)
        );

        assertEquals("A user with 'janedoe@example.com' already exists", thrown.getMessage());
        verify(userRepository, never()).save(any());
    }
}