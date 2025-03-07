package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.response.room.*;
import co.edu.udea.salasinfo.mapper.request.RoomRequestMapper;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    private static final Long ROOM_ID = 1L;
    private static final LocalDateTime RESERVATION_START = LocalDateTime.of(2025, 2, 15, 10, 0);
    private static final LocalDateTime RESERVATION_END = LocalDateTime.of(2025, 2, 15, 12, 0);

    @Mock
    private RoomDAO roomDAO;

    @Mock
    private RoomRequestMapper roomRequestMapper;

    @Mock
    private ImplementDAO implementDAO;

    @Mock
    private RestrictionDAO restrictionDAO;

    @Mock
    private ApplicationDAO applicationDAO;


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
    void findRoomSchedule_ShouldReturnSchedule_WhenRoomHasReservations() {
        when(roomDAO.findById(ROOM_ID)).thenReturn(room);
        when(roomScheduleResponseMapper.toResponses(List.of(reservation))).thenReturn(List.of(new RoomScheduleResponse()));

        List<RoomScheduleResponse> schedule = roomService.findRoomSchedule(ROOM_ID);

        assertFalse(schedule.isEmpty());
        verify(roomDAO, times(1)).findById(ROOM_ID);
        verify(roomScheduleResponseMapper, times(1)).toResponses(List.of(reservation));
    }

}
