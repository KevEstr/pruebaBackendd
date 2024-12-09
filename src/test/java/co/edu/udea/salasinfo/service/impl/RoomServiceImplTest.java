package co.edu.udea.salasinfo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.SpecificRoomResponse;
import co.edu.udea.salasinfo.mapper.request.RoomRequestMapper;
import co.edu.udea.salasinfo.mapper.response.RoomResponseMapper;
import co.edu.udea.salasinfo.mapper.response.RoomScheduleResponseMapper;
import co.edu.udea.salasinfo.mapper.response.SpecificRoomResponseMapper;
import co.edu.udea.salasinfo.model.*;
import co.edu.udea.salasinfo.persistence.*;
import co.edu.udea.salasinfo.utils.enums.ImplementCondition;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomDAO roomDAO;

    @Mock
    private ReservationDAO reservationDAO;

    @Mock
    private RoomImplementDAO roomImplementDAO;

    @Mock
    private RoomApplicationDAO roomApplicationDAO;

    @Mock
    private RoomRestrictionDAO roomRestrictionDAO;

    @Mock
    private ImplementDAO implementDAO;

    @Mock
    private ApplicationDAO applicationDAO;

    @Mock
    private RestrictionDAO restrictionDAO;

    @Mock
    private RoomResponseMapper roomResponseMapper;

    @Mock
    private RoomRequestMapper roomRequestMapper;

    @Mock
    private SpecificRoomResponseMapper specificRoomResponseMapper;

    @Mock
    private RoomScheduleResponseMapper roomScheduleResponseMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private Room mockRoom;
    @Mock
    private Reservation mockReservation;
    @Mock
    private Implement mockImplement;
    @Mock
    private Application mockApplication;

    private RoomResponse mockRoomResponse;
    private RoomRequest mockRoomRequest;
    private SpecificRoomResponse mockSpecificRoomResponse;

    @BeforeEach
    void setUp() {
        Reservation mockReservation;
        mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setActivityName("Meeting");
        mockReservation.setActivityDescription("Team Meeting");
        mockReservation.setStartsAt(LocalDateTime.now().plusDays(1));
        mockReservation.setEndsAt(LocalDateTime.now().plusDays(1).plusHours(1));
        mockReservation.setReservationState(new ReservationState(1L, RStatus.PENDING));

        mockImplement = new Implement();
        mockImplement.setId(1L);

        mockRoom = new Room();
        mockRoom.setId(123L);
        mockRoom.setBuilding("2");
        mockRoom.setRoomNum("101");
        mockRoom.setRoomName("Conference Room");
        mockRoom.setComputerAmount(10);
        mockRoom.setSubRoom(0);
        mockRoom.setReservations(List.of(mockReservation));

        mockRoomResponse = new RoomResponse();
        mockRoomResponse.setId(1);
        mockRoomResponse.setBuilding("2");
        mockRoomResponse.setRoomNum("101");
        mockRoomResponse.setRoomName("Conference Room");
        mockRoomResponse.setComputerAmount(10);
        mockRoomResponse.setSubRoom(0);

        mockRoomRequest = new RoomRequest();
        mockRoomRequest.setComputerAmount(10);
        mockRoomRequest.setBuilding("2");
        mockRoomRequest.setRoomNum("101");
        mockRoomRequest.setRoomName("Conference Room");
        mockRoomRequest.setSubRoom(0);
    }

    @Test
    void findAll_ReturnsListOfRooms() {
        // Arrange
        RoomFilter filter = new RoomFilter(); // Assume you have a valid RoomFilter implementation
        when(roomDAO.findAll(filter)).thenReturn(Collections.singletonList(mockRoom));
        when(roomResponseMapper.toResponses(any())).thenReturn(Collections.singletonList(mockRoomResponse));

        // Act
        List<RoomResponse> responses = roomService.findAll(filter);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(mockRoomResponse, responses.get(0));
    }

    @Test
    void findById_ReturnsSpecificRoomResponse() {
        // Arrange
        when(roomDAO.findById(anyLong())).thenReturn(mockRoom);
        when(specificRoomResponseMapper.toResponse(any())).thenReturn(new SpecificRoomResponse());

        // Act
        SpecificRoomResponse response = roomService.findById(1L);

        // Assert
        assertNotNull(response);
        verify(roomDAO).findById(1L);
    }

    @Test
    void createRoom_Success() {
        // Arrange
        when(roomRequestMapper.toEntity(any(RoomRequest.class))).thenReturn(mockRoom);
        when(roomDAO.existsById(anyLong())).thenReturn(false); // La sala no existe aún
        when(roomDAO.save(any(Room.class))).thenReturn(mockRoom);
        when(specificRoomResponseMapper.toResponse(isNull())).thenReturn(mockSpecificRoomResponse);
        when(specificRoomResponseMapper.toResponse(any(Room.class))).thenReturn(mockSpecificRoomResponse);

        // Simular búsquedas de DAO para implementos, software y restricciones
        Implement mockImplement = new Implement(1L, "Projector", null);
        Application mockApplication = new Application(1L, "Zoom", null);
        Restriction mockRestriction = new Restriction(1L, "No food allowed", null);

        when(implementDAO.findById(anyLong())).thenReturn(mockImplement);
        when(applicationDAO.findById(anyLong())).thenReturn(mockApplication);
        when(restrictionDAO.findAllById(anyList())).thenReturn(List.of(mockRestriction));

        // Configurar el mock del RoomRequest
        mockRoomRequest.setImplementIds(List.of(1L));
        mockRoomRequest.setImplementStates(List.of(ImplementCondition.Bueno));
        mockRoomRequest.setSoftwareIds(List.of(1L));
        mockRoomRequest.setSoftwareVersions(List.of("5.0.1"));
        mockRoomRequest.setRestrictionIds(List.of(1L));

        // Act
        SpecificRoomResponse response = roomService.createRoom(mockRoomRequest);

        // Assert
        assertNotNull(response);
        assertEquals(mockRoomResponse, response);

        // Verificar que la sala fue guardada
        verify(roomDAO, times(2)).save(any(Room.class));

        // Verificar que los implementos y software fueron procesados
        verify(implementDAO, times(1)).findById(anyLong());
        verify(applicationDAO, times(1)).findById(anyLong());
        verify(restrictionDAO, times(1)).findAllById(anyList());
        verify(roomImplementDAO, times(1)).save(any(RoomImplement.class));
        verify(roomApplicationDAO, times(1)).save(any(RoomApplication.class));
    }


    @Test
    void createRoom_ThrowsEntityNotFoundException() {
        // Arrange
        when(roomDAO.findById(anyLong())).thenThrow(new EntityNotFoundException(Room.class.getSimpleName()));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> roomService.createRoom(mockRoomRequest));
        verify(roomDAO, never()).save(any());
    }

    @Test
    void updateRoom_UpdatesRoomAndRelatedEntities() {
        // Arrange
        when(roomDAO.findById(1L)).thenReturn(mockRoom);
        when(implementDAO.findById(anyLong())).thenReturn(mockImplement);
        when(applicationDAO.findById(anyLong())).thenReturn(mockApplication);
        when(roomDAO.save(any())).thenReturn(mockRoom);
        when(specificRoomResponseMapper.toResponse(any())).thenReturn(mockSpecificRoomResponse);

        // Mock RoomRequest con datos nuevos
        RoomRequest mockRoomRequest = new RoomRequest();
        mockRoomRequest.setRoomName("Updated Room");
        mockRoomRequest.setComputerAmount(10);
        mockRoomRequest.setImplementIds(Arrays.asList(1L, 2L));
        mockRoomRequest.setImplementStates(Arrays.asList(ImplementCondition.Bueno, ImplementCondition.Malo));
        mockRoomRequest.setSoftwareIds(Arrays.asList(1L));
        mockRoomRequest.setSoftwareVersions(Arrays.asList("v1.0"));
        mockRoomRequest.setRestrictionIds(Arrays.asList(1L));

        // Act
        SpecificRoomResponse response = roomService.updateRoom(1L, mockRoomRequest);

        // Assert
        assertNotNull(response);
        assertEquals(mockSpecificRoomResponse, response);

        // Verificar que se actualizaron los campos básicos
        verify(mockRoom).setRoomName("Updated Room");
        verify(mockRoom).setComputerAmount(10);

        // Verificar eliminación de implementos y software antiguos
        verify(roomImplementDAO).deleteAll(any());
        verify(roomApplicationDAO).deleteAll(any());

        // Verificar guardado de nuevos implementos y software
        verify(roomImplementDAO, times(2)).save(any(RoomImplement.class));
        verify(roomApplicationDAO).save(any(RoomApplication.class));

        // Verificar eliminación y guardado de restricciones
        verify(roomRestrictionDAO).deleteAllByRoomId(1L);
        verify(mockRoom).setRestrictions(any());

        // Verificar guardado final del room
        verify(roomDAO).save(mockRoom);
    }


    @Test
    void deleteRoom_RemovesRoomAndRelatedEntities() {
        // Arrange
        when(roomDAO.findById(1L)).thenReturn(mockRoom);
        when(roomResponseMapper.toResponse(any())).thenReturn(mockRoomResponse);
        doNothing().when(roomDAO).deleteById(1L);
        doNothing().when(roomImplementDAO).deleteAllByRoomId(1L);
        doNothing().when(roomApplicationDAO).deleteAllByRoomId(1L);
        doNothing().when(roomRestrictionDAO).deleteAllByRoomId(1L);

        // Simular una sala sin reservas
        when(mockRoom.getReservations()).thenReturn(Collections.emptyList());

        // Act
        RoomResponse response = roomService.deleteRoom(1L);

        // Assert
        assertNotNull(response);
        assertEquals(mockRoomResponse, response);

        // Verificar que se eliminaron las relaciones
        verify(roomImplementDAO).deleteAllByRoomId(1L);
        verify(roomApplicationDAO).deleteAllByRoomId(1L);
        verify(roomRestrictionDAO).deleteAllByRoomId(1L);

        // Verificar que se eliminó la sala
        verify(roomDAO).deleteById(1L);
    }

    @Test
    void deleteRoom_ThrowsException_WhenRoomHasReservations() {
        // Arrange
        when(roomDAO.findById(1L)).thenReturn(mockRoom);
        when(mockRoom.getReservations()).thenReturn(Collections.singletonList(mockReservation));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roomService.deleteRoom(1L);
        });

        assertEquals("No se puede eliminar la sala porque tiene reservas asociadas", exception.getMessage());

        // Verificar que no se intentó eliminar nada
        verify(roomDAO, never()).deleteById(anyLong());
        verify(roomImplementDAO, never()).deleteAllByRoomId(anyLong());
        verify(roomApplicationDAO, never()).deleteAllByRoomId(anyLong());
        verify(roomRestrictionDAO, never()).deleteAllByRoomId(anyLong());
    }


    @Test
    void findFreeAt_ReturnsFreeRooms() {
        LocalDateTime date = LocalDateTime.now();
        Reservation mockReservation = new Reservation();
        mockReservation.setStartsAt(date.minusHours(1));
        mockReservation.setEndsAt(date.plusHours(1));
        mockReservation.setRoom(mockRoom);
        mockReservation.setReservationState(new ReservationState(1L, RStatus.ACCEPTED));

        when(reservationDAO.findAll()).thenReturn(List.of(mockReservation));
        when(roomDAO.findAll(null)).thenReturn(Collections.singletonList(mockRoom));
        when(roomResponseMapper.toResponses(any())).thenReturn(Collections.singletonList(mockRoomResponse));

        List<RoomResponse> freeRooms = roomService.findFreeAt(date);

        assertNotNull(freeRooms);
        assertFalse(freeRooms.isEmpty());
    }

    @Test
    void findRoomSchedule_ReturnsRoomSchedule() {
        // Arrange
        when(roomDAO.findById(anyLong())).thenReturn(mockRoom);
        when(roomScheduleResponseMapper.toResponses(any())).thenReturn(List.of(new RoomScheduleResponse()));

        // Act
        List<RoomScheduleResponse> schedule = roomService.findRoomSchedule(1L);

        // Assert
        assertNotNull(schedule);
        assertEquals(1, schedule.size());
    }
}
