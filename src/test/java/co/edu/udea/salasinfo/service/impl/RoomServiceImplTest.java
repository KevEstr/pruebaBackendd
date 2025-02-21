package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.response.room.*;
import co.edu.udea.salasinfo.mapper.request.RoomRequestMapper;
import co.edu.udea.salasinfo.mapper.response.RoomResponseMapper;
import co.edu.udea.salasinfo.mapper.response.RoomScheduleResponseMapper;
import co.edu.udea.salasinfo.mapper.response.SpecificRoomResponseMapper;
import co.edu.udea.salasinfo.model.*;
import co.edu.udea.salasinfo.persistence.*;
import co.edu.udea.salasinfo.utils.enums.ImplementCondition;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    private static final Long ROOM_ID = 1L;
    private static final LocalDateTime RESERVATION_START = LocalDateTime.of(2025, 2, 15, 10, 0);
    private static final LocalDateTime RESERVATION_END = LocalDateTime.of(2025, 2, 15, 12, 0);
    private static final LocalDate SELECTED_DATE = LocalDate.of(2025, 2, 15);
    private static final LocalTime HOUR_10_AM = LocalTime.of(10, 0);
    private static final LocalTime HOUR_11_AM = LocalTime.of(11, 0);
    private static final LocalTime SIX_AM = LocalTime.of(6, 0);
    private static final LocalTime NINE_PM = LocalTime.of(21, 0);

    @Mock
    private RoomDAO roomDAO;

    @Mock
    private ReservationDAO reservationDAO;

    @Mock
    private RoomResponseMapper roomResponseMapper;

    @Mock
    private RoomRequestMapper roomRequestMapper;

    @Mock
    private ImplementDAO implementDAO;

    @Mock
    private RestrictionDAO restrictionDAO;

    @Mock
    private ApplicationDAO applicationDAO;

    @Mock
    private RoomImplementDAO roomImplementDAO;

    @Mock
    private RoomApplicationDAO roomApplicationDAO;

    @Mock
    private RoomRestrictionDAO roomRestrictionDAO;


    @Mock
    private SpecificRoomResponseMapper specificRoomResponseMapper;

    @Mock
    private RoomScheduleResponseMapper roomScheduleResponseMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(ROOM_ID);
        room.setReservations(new ArrayList<>()); // Ensure it's initialized

        ReservationState acceptedState = new ReservationState();
        acceptedState.setState(RStatus.ACCEPTED);

        reservation = new Reservation();
        reservation.setStartsAt(RESERVATION_START);
        reservation.setEndsAt(RESERVATION_END);
        reservation.setReservationState(acceptedState);
        reservation.setRoom(room);

        room.getReservations().add(reservation);
    }

    @Test
    void findFreeAt_ShouldReturnEmptyList_WhenNoRoomsAreAvailable() {
        when(reservationDAO.findAll()).thenReturn(List.of(reservation));
        when(roomDAO.findAll(null)).thenReturn(List.of(room)); // Only one room, but it's occupied

        List<RoomResponse> freeRooms = roomService.findFreeAt(RESERVATION_START);

        assertTrue(freeRooms.isEmpty());
        verify(roomDAO, times(1)).findAll(null);
        verify(reservationDAO, times(1)).findAll();
    }

    @Test
    void findRoomSchedule_ShouldReturnSchedule_WhenRoomHasReservations() {
        when(roomDAO.findById(ROOM_ID)).thenReturn(room);
        when(roomScheduleResponseMapper.toResponses(List.of(reservation))).thenReturn(List.of(new RoomScheduleResponse()));

        List<RoomScheduleResponse> schedule = roomService.findRoomSchedule(ROOM_ID);

        assertFalse(schedule.isEmpty());
        verify(roomDAO, times(1)).findById(ROOM_ID);
        verify(roomScheduleResponseMapper, times(1)).toResponses(List.of(reservation));
    }

    @Test
    void findFreeRoomSchedule_ShouldReturnAvailableHours_WhenRoomHasReservations() {
        when(roomDAO.findById(ROOM_ID)).thenReturn(room);

        FreeRoomScheduleResponse freeRoomSchedule = roomService.findFreeRoomSchedule(ROOM_ID, SELECTED_DATE);

        // Verifica que en la lista de horarios de inicio se incluya la hora de las 6:00
        assertFalse(freeRoomSchedule.getFreeStartTimes()
                .stream()
                .anyMatch(ts -> ts.getHour().equals(HOUR_10_AM)));

        // Verifica que en la lista de horarios de fin se incluya la hora de las 21:00
        assertFalse(freeRoomSchedule.getFreeEndTimes()
                .stream()
                .anyMatch(ts -> ts.getHour().equals(HOUR_11_AM)));
        verify(roomDAO, times(1)).findById(ROOM_ID);
    }

    @Test
    void findFreeRoomSchedule_ShouldReturnAllHours_WhenRoomHasNoReservations() {
        room.setReservations(Collections.emptyList()); // No reservations

        when(roomDAO.findById(ROOM_ID)).thenReturn(room);

        FreeRoomScheduleResponse freeRoomSchedule = roomService.findFreeRoomSchedule(ROOM_ID, SELECTED_DATE);

        // Verifica que en la lista de horarios de inicio se incluya la hora de las 6:00
        assertTrue(freeRoomSchedule.getFreeStartTimes()
                .stream()
                .anyMatch(ts -> ts.getHour().equals(SIX_AM)));

        // Verifica que en la lista de horarios de fin se incluya la hora de las 21:00
        assertTrue(freeRoomSchedule.getFreeEndTimes()
                .stream()
                .anyMatch(ts -> ts.getHour().equals(NINE_PM)));

        verify(roomDAO, times(1)).findById(ROOM_ID);
    }

    @Test
    void findFreeRoomSchedule_ShouldReturnOnlyNonOverlappingHours_WhenMultipleReservationsExist() {
        ReservationState acceptedState = new ReservationState();
        acceptedState.setState(RStatus.ACCEPTED);

        Reservation secondReservation = new Reservation();
        secondReservation.setStartsAt(LocalDateTime.of(2025, 2, 15, 14, 0));
        secondReservation.setEndsAt(LocalDateTime.of(2025, 2, 15, 16, 0));
        secondReservation.setReservationState(acceptedState);
        secondReservation.setRoom(room);

        room.getReservations().add(secondReservation);

        when(roomDAO.findById(ROOM_ID)).thenReturn(room);

        FreeRoomScheduleResponse freeRoomSchedule = roomService.findFreeRoomSchedule(ROOM_ID, SELECTED_DATE);


        assertFalse(freeRoomSchedule.getFreeStartTimes()
                .stream()
                .anyMatch(ts -> ts.getHour().equals(HOUR_10_AM)));// Reserved


        assertFalse(freeRoomSchedule.getFreeEndTimes()
                .stream()
                .anyMatch(ts -> ts.getHour().equals(HOUR_11_AM)));// Reserved


        assertFalse(freeRoomSchedule.getFreeStartTimes()
                .stream()
                .anyMatch(ts -> ts.getHour().equals(LocalTime.of(14, 0))));// Second reservation


        assertFalse(freeRoomSchedule.getFreeEndTimes()
                .stream()
                .anyMatch(ts -> ts.getHour().equals(LocalTime.of(15, 0))));// Second reservation

        verify(roomDAO, times(1)).findById(ROOM_ID);
    }

    @Test
    void createRoom_ShouldCreateRoomSuccessfully_WhenValidRequestIsProvided() {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setBuilding("1");
        roomRequest.setRoomNum("101");
        roomRequest.setSubRoom(1);
        roomRequest.setImplementIds(List.of(1L));
        roomRequest.setImplementStates(List.of(ImplementCondition.Bueno));
        roomRequest.setSoftwareIds(List.of(1L));
        roomRequest.setSoftwareVersions(List.of("1.0"));
        roomRequest.setRestrictionIds(List.of(1L));

        Long expectedRoomId = 11011L;
        Room mappedRoom = new Room();
        mappedRoom.setId(expectedRoomId);

        Implement mockImplement = new Implement();
        mockImplement.setId(1L);

        Application mockApplication = new Application();
        mockApplication.setId(1L);

        Restriction mockRestriction = new Restriction();
        mockRestriction.setId(1L);

        when(roomDAO.existsById(expectedRoomId)).thenReturn(false);
        when(roomRequestMapper.toEntity(roomRequest)).thenReturn(mappedRoom);
        when(implementDAO.findById(1L)).thenReturn(mockImplement);
        when(applicationDAO.findById(1L)).thenReturn(mockApplication);
        when(restrictionDAO.findAllById(List.of(1L))).thenReturn(List.of(mockRestriction));
        when(roomDAO.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(roomDAO.findById(expectedRoomId)).thenReturn(mappedRoom);
        when(specificRoomResponseMapper.toResponse(any(Room.class))).thenReturn(new SpecificRoomResponse());

        SpecificRoomResponse response = roomService.createRoom(roomRequest);

        assertNotNull(response);
        verify(roomDAO, times(2)).save(any(Room.class));
        verify(roomDAO, times(1)).findById(expectedRoomId);
        verify(implementDAO, times(1)).findById(1L);
        verify(applicationDAO, times(1)).findById(1L);
        verify(restrictionDAO, times(1)).findAllById(List.of(1L));
    }

    @Test
    void updateRoom_ShouldUpdateRoomSuccessfully_WhenValidRequestIsProvided() {
        // Given
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setRoomName("Updated Room");
        roomRequest.setComputerAmount(15);
        roomRequest.setImplementIds(List.of(1L));
        roomRequest.setImplementStates(List.of(ImplementCondition.Malo));
        roomRequest.setSoftwareIds(List.of(1L));
        roomRequest.setSoftwareVersions(List.of("2.0"));
        roomRequest.setRestrictionIds(List.of(1L));

        Implement mockImplement = new Implement();
        mockImplement.setId(1L);

        Application mockApplication = new Application();
        mockApplication.setId(1L);

        Restriction mockRestriction = new Restriction();
        mockRestriction.setId(1L);

        Room foundRoom = new Room();
        foundRoom.setId(ROOM_ID);
        foundRoom.setRoomName("Old Room");
        foundRoom.setComputerAmount(10);
        foundRoom.setImplementsList(Collections.emptyList());
        foundRoom.setRoomApplications(Collections.emptyList());
        foundRoom.setRestrictions(Collections.emptyList());

        when(roomDAO.findById(ROOM_ID)).thenReturn(foundRoom);
        when(implementDAO.findById(1L)).thenReturn(mockImplement);
        when(applicationDAO.findById(1L)).thenReturn(mockApplication);
        when(restrictionDAO.findAllById(List.of(1L))).thenReturn(List.of(mockRestriction));
        when(roomDAO.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(roomDAO.findById(ROOM_ID)).thenReturn(foundRoom);
        when(specificRoomResponseMapper.toResponse(any(Room.class))).thenReturn(new SpecificRoomResponse());

        // When
        SpecificRoomResponse response = roomService.updateRoom(ROOM_ID, roomRequest);

        // Then
        assertNotNull(response);
        assertEquals("Updated Room", foundRoom.getRoomName());
        assertEquals(15, foundRoom.getComputerAmount());
        assertEquals(1, foundRoom.getImplementsList().size());
        assertEquals(ImplementCondition.Malo.toString(), foundRoom.getImplementsList().get(0).getState());
        assertEquals(1, foundRoom.getRoomApplications().size());
        assertEquals("2.0", foundRoom.getRoomApplications().get(0).getVersion());
        assertEquals(1, foundRoom.getRestrictions().size());

        verify(roomDAO, times(2)).findById(ROOM_ID);
        verify(roomDAO, times(1)).save(foundRoom);
        verify(implementDAO, times(1)).findById(1L);
        verify(applicationDAO, times(1)).findById(1L);
        verify(restrictionDAO, times(1)).findAllById(List.of(1L));
        verify(roomImplementDAO, times(1)).deleteAll(anyList());
        verify(roomApplicationDAO, times(1)).deleteAll(anyList());
        verify(roomRestrictionDAO, times(1)).deleteAllByRoomId(ROOM_ID);
    }


}
