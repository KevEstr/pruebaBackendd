package co.edu.udea.salasinfo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.dto.response.room.FreeScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.SpecificRoomResponse;
import co.edu.udea.salasinfo.mapper.request.RoomRequestMapper;
import co.edu.udea.salasinfo.mapper.response.RoomResponseMapper;
import co.edu.udea.salasinfo.mapper.response.RoomScheduleResponseMapper;
import co.edu.udea.salasinfo.mapper.response.SpecificRoomResponseMapper;
import co.edu.udea.salasinfo.model.Reservation;
import co.edu.udea.salasinfo.model.ReservationState;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.persistence.ReservationDAO;
import co.edu.udea.salasinfo.persistence.RoomDAO;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomDAO roomDAO;

    @Mock
    private ReservationDAO reservationDAO;

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

    private Room mockRoom;
    private RoomResponse mockRoomResponse;
    private RoomRequest mockRoomRequest;

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

        when(roomDAO.findById(anyLong())).thenReturn(mockRoom);
        when(roomRequestMapper.toEntity(any())).thenReturn(mockRoom);
        when(roomDAO.save(any())).thenReturn(mockRoom);
        when(roomResponseMapper.toResponse(any())).thenReturn(mockRoomResponse);

        // Act
        RoomResponse response = roomService.createRoom(mockRoomRequest);

        // Assert
        assertNotNull(response);
        assertEquals(mockRoomResponse, response);
        verify(roomDAO).save(any());
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
    void updateRoom_UpdatesRoom() {
        // Arrange
        when(roomDAO.findById(anyLong())).thenReturn(mockRoom);
        when(roomDAO.save(any())).thenReturn(mockRoom);
        when(roomResponseMapper.toResponse(any())).thenReturn(mockRoomResponse);

        // Act
        RoomResponse response = roomService.updateRoom(1L, mockRoomRequest);

        // Assert
        assertNotNull(response);
        assertEquals(mockRoomResponse, response);
        verify(roomDAO).save(mockRoom);
    }

    @Test
    void deleteRoom_RemovesRoom() {
        // Arrange
        when(roomDAO.findById(anyLong())).thenReturn(mockRoom);
        when(roomResponseMapper.toResponse(any())).thenReturn(mockRoomResponse);
        doNothing().when(roomDAO).deleteById(anyLong());

        // Act
        RoomResponse response = roomService.deleteRoom(1L);

        // Assert
        assertNotNull(response);
        assertEquals(mockRoomResponse, response);
        verify(roomDAO).deleteById(1L);
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

    @Test
    void findFreeRoomSchedule_ReturnsFreeSchedule() {
        // Arrange
        Long roomId = 123L;
        LocalDate selectedDate = LocalDate.now();
        Room mockRoom = new Room();
        mockRoom.setId(roomId);

        Reservation reservation1 = new Reservation();
        reservation1.setStartsAt(LocalDateTime.of(selectedDate, LocalTime.of(10, 0)));
        reservation1.setEndsAt(LocalDateTime.of(selectedDate, LocalTime.of(12, 0)));
        reservation1.setReservationState(new ReservationState(1L, RStatus.ACCEPTED));

        Reservation reservation2 = new Reservation();
        reservation2.setStartsAt(LocalDateTime.of(selectedDate, LocalTime.of(14, 0)));
        reservation2.setEndsAt(LocalDateTime.of(selectedDate, LocalTime.of(15, 0)));
        reservation2.setReservationState(new ReservationState(1L, RStatus.ACCEPTED));

        mockRoom.setReservations(List.of(reservation1, reservation2));

        when(roomDAO.findById(roomId)).thenReturn(mockRoom);

        // Act
        List<FreeScheduleResponse> freeSchedules = roomService.findFreeRoomSchedule(roomId, selectedDate);

        // Assert
        assertNotNull(freeSchedules);
        List<LocalTime> expectedFreeHours = List.of(
                LocalTime.of(6, 0),
                LocalTime.of(7, 0),
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                LocalTime.of(17, 0),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                LocalTime.of(20, 0),
                LocalTime.of(21, 0)
        );

        List<LocalTime> actualFreeHours = freeSchedules.stream()
                .map(FreeScheduleResponse::getHour)
                .collect(Collectors.toList());

        assertEquals(expectedFreeHours, actualFreeHours);
        verify(roomDAO).findById(roomId);
    }
}
