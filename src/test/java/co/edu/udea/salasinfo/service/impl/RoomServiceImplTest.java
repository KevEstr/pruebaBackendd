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
import co.edu.udea.salasinfo.utils.enums.RStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
        Reservation reservationMock;
        reservationMock = new Reservation();
        reservationMock.setId(1L);
        reservationMock.setActivityName("Meeting");
        reservationMock.setActivityDescription("Team Meeting");
        reservationMock.setStartsAt(LocalDateTime.now().plusDays(1));
        reservationMock.setEndsAt(LocalDateTime.now().plusDays(1).plusHours(1));
        reservationMock.setReservationState(new ReservationState(1L, RStatus.PENDING));

        mockImplement = new Implement();
        mockImplement.setId(1L);

        mockRoom = new Room();
        mockRoom.setId(123L);
        mockRoom.setBuilding("2");
        mockRoom.setRoomNum("101");
        mockRoom.setRoomName("Conference Room");
        mockRoom.setComputerAmount(10);
        mockRoom.setSubRoom(0);
        mockRoom.setReservations(List.of(reservationMock));

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

        mockSpecificRoomResponse = new SpecificRoomResponse();
        mockSpecificRoomResponse.setId(123);
        mockSpecificRoomResponse.setBuilding("2");
        mockSpecificRoomResponse.setRoomNum("101");
        mockSpecificRoomResponse.setRoomName("Conference Room");
        mockSpecificRoomResponse.setComputerAmount(10);
        mockSpecificRoomResponse.setSubRoom(0);
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
    void findFreeAt_ReturnsFreeRooms() {
        LocalDateTime date = LocalDateTime.now();
        Reservation reservation = new Reservation();
        reservation.setStartsAt(date.minusHours(1));
        reservation.setEndsAt(date.plusHours(1));
        reservation.setRoom(mockRoom);
        reservation.setReservationState(new ReservationState(1L, RStatus.ACCEPTED));

        when(reservationDAO.findAll()).thenReturn(List.of(reservation));
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
