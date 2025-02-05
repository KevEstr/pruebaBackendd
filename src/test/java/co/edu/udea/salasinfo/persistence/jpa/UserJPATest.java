package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.User;
import co.edu.udea.salasinfo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJPATest {
/*
    @InjectMocks
    private UserJPA userJPA;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id("1")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userJPA.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void testFindByEmail_Success() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        User foundUser = userJPA.findByEmail("john.doe@example.com");

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> userJPA.findByEmail("notfound@example.com"));

        assertEquals("User with email 'notfound@example.com' not found", thrown.getMessage());
        verify(userRepository).findByEmail("notfound@example.com");
    }

    @Test
    void testSave() {
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userJPA.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        verify(userRepository).save(user);
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User foundUser = userJPA.findById("1");

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> userJPA.findById("1"));

        assertEquals("Entity of 'User' type searched with '1' not found", thrown.getMessage());
        verify(userRepository).findById("1");
    }

    @Test
    void testExistsByUsername() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        Boolean exists = userJPA.existsByUsername("john.doe@example.com");

        assertTrue(exists);
        verify(userRepository).existsByEmail("john.doe@example.com");
    }

    @Test
    void testExistsByCustomerId() {
        when(userRepository.existsById("1")).thenReturn(true);

        Boolean exists = userJPA.existsByCustomerId("1");

        assertTrue(exists);
        verify(userRepository).existsById("1");
    }


 */
}
