package co.edu.udea.salasinfo.configuration.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @Value("${jwt.secret_key}")
    private String secret = "34e47b7f3942428c7521318b21e3978cf083fa7773a9bc03d7eb0b4f5467d779"; // Update this to your actual secret

    @Value("${jwt.expiration_time}")
    private Integer expirationTime = 3600000; // Example: 1 hour

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService(secret, expirationTime);

        // Mock UserDetails
        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    void testGenerateToken() {
        // Arrange
        Map<String, String> claims = new HashMap<>();
        claims.put("role", "Usuario");

        // Act
        String token = jwtService.generateToken(claims, userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.startsWith("ey")); // JWTs start with 'ey'
    }

    @Test
    void testExtractUsername() {
        // Arrange
        Map<String, String> claims = new HashMap<>();
        claims.put("role", "Usuario");
        String token = jwtService.generateToken(claims, userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testUser", username);
    }

    @Test
    void testGetClaim() {
        // Arrange
        Map<String, String> claims = new HashMap<>();
        claims.put("role", "Usuario");
        String token = jwtService.generateToken(claims, userDetails);

        // Act
        String role = jwtService.getClaim(token, claimsMap -> claimsMap.get("role").toString());

        // Assert
        assertEquals("Usuario", role);
    }

    @Test
    void testIsTokenValid() {
        // Arrange
        Map<String, String> claims = new HashMap<>();
        claims.put("role", "Usuario");
        String token = jwtService.generateToken(claims, userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsTokenValid_InvalidUsername() {
        // Arrange
        Map<String, String> claims = new HashMap<>();
        claims.put("role", "Usuario");
        String token = jwtService.generateToken(claims, userDetails);

        UserDetails invalidUserDetails = mock(UserDetails.class);
        when(invalidUserDetails.getUsername()).thenReturn("invalidUser");

        // Act
        boolean isValid = jwtService.isTokenValid(token, invalidUserDetails);

        // Assert
        assertFalse(isValid);
    }
}
