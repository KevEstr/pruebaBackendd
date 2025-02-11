package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.response.room.FreeScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomScheduleResponse;
import co.edu.udea.salasinfo.mapper.response.RoomResponseMapper;
import co.edu.udea.salasinfo.mapper.response.RoomScheduleResponseMapper;
import co.edu.udea.salasinfo.model.Reservation;
import co.edu.udea.salasinfo.model.ReservationState;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.persistence.ReservationDAO;
import co.edu.udea.salasinfo.persistence.RoomDAO;
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

        List<FreeScheduleResponse> freeHours = roomService.findFreeRoomSchedule(ROOM_ID, SELECTED_DATE);

        assertFalse(freeHours.stream().anyMatch(hour -> hour.getHour().equals(HOUR_10_AM)));
        assertFalse(freeHours.stream().anyMatch(hour -> hour.getHour().equals(HOUR_11_AM)));
        verify(roomDAO, times(1)).findById(ROOM_ID);
    }

    @Test
    void findFreeRoomSchedule_ShouldReturnAllHours_WhenRoomHasNoReservations() {
        room.setReservations(Collections.emptyList()); // No reservations

        when(roomDAO.findById(ROOM_ID)).thenReturn(room);

        List<FreeScheduleResponse> freeHours = roomService.findFreeRoomSchedule(ROOM_ID, SELECTED_DATE);

        assertTrue(freeHours.stream().anyMatch(hour -> hour.getHour().equals(SIX_AM)));
        assertTrue(freeHours.stream().anyMatch(hour -> hour.getHour().equals(NINE_PM)));
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

        List<FreeScheduleResponse> freeHours = roomService.findFreeRoomSchedule(ROOM_ID, SELECTED_DATE);

        assertFalse(freeHours.stream().anyMatch(hour -> hour.getHour().equals(HOUR_10_AM))); // Reserved
        assertFalse(freeHours.stream().anyMatch(hour -> hour.getHour().equals(HOUR_11_AM))); // Reserved
        assertFalse(freeHours.stream().anyMatch(hour -> hour.getHour().equals(LocalTime.of(14, 0)))); // Second reservation
        assertFalse(freeHours.stream().anyMatch(hour -> hour.getHour().equals(LocalTime.of(15, 0)))); // Second reservation
        verify(roomDAO, times(1)).findById(ROOM_ID);
    }
}
