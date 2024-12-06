package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.persistence.RoomDAO;
import co.edu.udea.salasinfo.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomJPATest {

    @InjectMocks
    private RoomJPA roomJPA;

    @Mock
    private RoomRepository roomRepository;

    private Room room;

    @Mock
    private RoomDAO roomDAO;

    @BeforeEach
    void setUp() {
        room = Room.builder()
                .id(1L)
                .computerAmount(20)
                .building("Main")
                .roomNum("101")
                .roomName("Computer Lab")
                .subRoom(1)
                .build();
    }

    @Test
    void testFindById_Success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room foundRoom = roomJPA.findById(1L);

        assertNotNull(foundRoom);
        assertEquals(room.getId(), foundRoom.getId());
        verify(roomRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> roomJPA.findById(1L));

        assertEquals("Entity of 'Room' type searched with '1' not found", thrown.getMessage());
        verify(roomRepository).findById(1L);
    }

    @Test
    void testSave() {
        when(roomRepository.save(room)).thenReturn(room);

        Room savedRoom = roomJPA.save(room);

        assertNotNull(savedRoom);
        assertEquals(room.getId(), savedRoom.getId());
        verify(roomRepository).save(room);
    }

    @Test
    void testDeleteById() {
        roomJPA.deleteById(1L);

        verify(roomRepository).deleteById(1L);
    }

    @Test
    void existsById_ReturnsTrue_WhenRoomExists() {
        // Arrange
        Long roomId = 1L;
        when(roomRepository.existsById(roomId)).thenReturn(true);

        // Act
        boolean exists = roomRepository.existsById(roomId);

        // Assert
        assertTrue(exists);
        verify(roomRepository, times(1)).existsById(roomId);
    }

    @Test
    void existsById_ReturnsFalse_WhenRoomDoesNotExist() {
        // Arrange
        Long roomId = 2L;
        when(roomRepository.existsById(roomId)).thenReturn(false);

        // Act
        boolean exists = roomRepository.existsById(roomId);

        // Assert
        assertFalse(exists);
        verify(roomRepository, times(1)).existsById(roomId);
    }


}