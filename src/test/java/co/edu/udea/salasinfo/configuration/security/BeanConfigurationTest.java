package co.edu.udea.salasinfo.configuration.security;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.repository.UserRepository;
import co.edu.udea.salasinfo.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeanConfigurationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private HandlerMappingIntrospector handlerMappingIntrospector;

    private BeanConfiguration beanConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        beanConfiguration = new BeanConfiguration(userRepository);
    }

    @Test
    void authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = beanConfiguration.authenticationManager(authenticationConfiguration);

        assertNotNull(result);
        assertEquals(authenticationManager, result);
    }

    @Test
    void userDetailService_userNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = beanConfiguration.userDetailService();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));

        assertEquals(String.format(Constants.USER_NOT_FOUND_MESSAGE, username), exception.getMessage());
    }

    @Test
    void mvcRequestMatcher() {
        MvcRequestMatcher.Builder mvcRequestMatcher = beanConfiguration.mvc(handlerMappingIntrospector);

        assertNotNull(mvcRequestMatcher);
    }

    @Test
    void jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = beanConfiguration.jackson2ObjectMapperBuilder();
        assertNotNull(builder);

        ObjectMapper objectMapper = builder.build();
        assertNotNull(objectMapper);
        assertEquals(StdDateFormat.class, objectMapper.getDateFormat().getClass());
    }
}
