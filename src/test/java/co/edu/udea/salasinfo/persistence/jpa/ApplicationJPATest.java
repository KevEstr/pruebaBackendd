package co.edu.udea.salasinfo.persistence.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Application;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.model.RoomApplication;
import co.edu.udea.salasinfo.persistence.ApplicationDAO;
import co.edu.udea.salasinfo.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplicationJPATest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationJPA applicationJPA;

    @Mock
    private Application mockApplication;

    @Mock
    private ApplicationDAO applicationDAO;

    @BeforeEach
    public void setUp() {
        // Initialize your mock objects
        mockApplication = Application.builder()
                .id(1L)
                .name("Test Application")
                .roomApplications(Collections.emptyList())
                .build();
    }

    @Test
    void testFindById() {
        // Arrange
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(mockApplication));

        // Act
        Application result = applicationJPA.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Application", result.getName());
        verify(applicationRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(applicationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> applicationJPA.findById(1L));
        assertEquals("Entity of 'Application' type searched with '1' not found", thrown.getMessage()); // Update based on your exception message implementation
        verify(applicationRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        // Arrange
        when(applicationRepository.findAll()).thenReturn(Collections.singletonList(mockApplication));

        // Act
        List<Application> result = applicationJPA.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Application", result.get(0).getName());
        verify(applicationRepository).findAll();
    }

    @Test
    void testFindRoomsByApplicationId() {
        // Arrange
        Room mockRoom = new Room();
        RoomApplication mockRoomApplication = new RoomApplication();
        mockRoomApplication.setRoom(mockRoom);
        mockRoomApplication.setApplication(mockApplication);

        mockApplication.setRoomApplications(Collections.singletonList(mockRoomApplication));

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(mockApplication));

        // Act
        List<Room> result = applicationJPA.findRoomsByApplicationId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockRoom, result.get(0));
    }

    @Test
    void testFindRoomsByApplicationId_NotFound() {
        // Arrange
        when(applicationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> applicationJPA.findRoomsByApplicationId(1L));
        assertEquals("Entity of 'Application' type searched with '1' not found", thrown.getMessage()); // Update based on your exception message implementation
        verify(applicationRepository).findById(1L);
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenNameExists() {
        // Arrange
        String name = "Zoom";
        when(applicationRepository.existsByName(name)).thenReturn(true);

        // Act
        boolean result = applicationDAO.existsByName(name);

        // Assert
        assertTrue(result);
        verify(applicationRepository, times(1)).existsByName(name);
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNameDoesNotExist() {
        // Arrange
        String name = "Slack";
        when(applicationRepository.existsByName(name)).thenReturn(false);

        // Act
        boolean result = applicationDAO.existsByName(name);

        // Assert
        assertFalse(result);
        verify(applicationRepository, times(1)).existsByName(name);
    }

}