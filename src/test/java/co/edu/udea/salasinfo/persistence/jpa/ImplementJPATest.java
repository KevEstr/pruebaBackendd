package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Implement;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.model.RoomImplement;
import co.edu.udea.salasinfo.persistence.ImplementDAO;
import co.edu.udea.salasinfo.repository.ImplementRepository;
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
class ImplementJPATest {

    @InjectMocks
    private ImplementJPA implementJPA;

    @Mock
    private ImplementRepository implementRepository;

    private Implement implement;

    @BeforeEach
    public void setUp() {
        implement = Implement.builder()
                .id(1L)
                .name("Projector")
                .roomImplements(Collections.emptyList())
                .build();
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(implementRepository.findById(1L)).thenReturn(Optional.of(implement));

        // Act
        Implement foundImplement = implementJPA.findById(1L);

        // Assert
        assertNotNull(foundImplement);
        assertEquals(implement.getId(), foundImplement.getId());
        verify(implementRepository).findById(1L);
    }

    @Test
    void testFindById_EntityNotFound() {
        // Arrange
        when(implementRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> implementJPA.findById(1L));

        assertEquals("Entity of 'Implement' type searched with '1' not found", thrown.getMessage());
        verify(implementRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        // Arrange
        when(implementRepository.findAll()).thenReturn(Collections.singletonList(implement));

        // Act
        List<Implement> implementsList = implementJPA.findAll();

        // Assert
        assertNotNull(implementsList);
        assertEquals(1, implementsList.size());
        assertEquals(implement.getId(), implementsList.get(0).getId());
        verify(implementRepository).findAll();
    }

    @Test
    void testFindRoomsByImplementId() {
        // Arrange
        Room room1 = new Room(); // Assume Room class has a proper constructor
        room1.setId(1L);
        room1.setRoomName("Room A");

        RoomImplement roomImplement = new RoomImplement(1L, room1, implement, "bueno");

        implement.setRoomImplements(List.of(roomImplement));
        when(implementRepository.findById(1L)).thenReturn(Optional.of(implement));

        // Act
        List<Room> rooms = implementJPA.findRoomsByImplementId(1L);

        // Assert
        assertNotNull(rooms);
        assertEquals(1, rooms.size());
        assertEquals(room1.getId(), rooms.get(0).getId());
        verify(implementRepository).findById(1L);
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenNameExists() {
        // Arrange
        String name = "Projector";
        when(implementRepository.existsByName(name)).thenReturn(true);  // Mock the repository call

        // Act
        boolean result = implementRepository.existsByName(name);

        // Assert
        assertTrue(result);
        verify(implementRepository, times(1)).existsByName(name);
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNameDoesNotExist() {
        // Arrange
        String name = "Whiteboard";
        when(implementRepository.existsByName(name)).thenReturn(false);  // Mock the repository call

        // Act
        boolean result = implementRepository.existsByName(name);  // Call the method on the repository

        // Assert
        assertFalse(result);  // Verify that the result is false
        verify(implementRepository, times(1)).existsByName(name);
    }

    @Test
    void deleteById_ShouldDeleteImplement() {
        // Arrange
        Long id = 1L;
        doNothing().when(implementRepository).deleteById(id); // Mock void method

        // Act
        implementJPA.deleteById(id);

        // Assert
        verify(implementRepository, times(1)).deleteById(id);
    }

    @Test
    void save_ShouldReturnSavedImplement() {
        // Arrange
        when(implementRepository.save(implement)).thenReturn(implement);

        // Act
        Implement savedImplement = implementJPA.save(implement);

        // Assert
        assertNotNull(savedImplement);
        assertEquals(implement.getId(), savedImplement.getId());
        verify(implementRepository, times(1)).save(implement);
    }

    @Test
    void findAllById_ShouldReturnListOfImplements() {
        // Arrange
        List<Long> ids = List.of(1L, 2L);
        Implement implement2 = Implement.builder().id(2L).name("Whiteboard").roomImplements(Collections.emptyList()).build();
        List<Implement> expectedImplements = List.of(implement, implement2);

        when(implementRepository.findAllById(ids)).thenReturn(expectedImplements);

        // Act
        List<Implement> result = implementJPA.findAllById(ids);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(implement.getId(), result.get(0).getId());
        assertEquals(implement2.getId(), result.get(1).getId());
        verify(implementRepository, times(1)).findAllById(ids);
    }


}
