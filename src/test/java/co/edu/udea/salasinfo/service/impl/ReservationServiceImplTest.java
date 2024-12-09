package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.request.ClassReservationRequest;
import co.edu.udea.salasinfo.dto.request.ReservationRequest;
import co.edu.udea.salasinfo.dto.request.SessionRequest;
import co.edu.udea.salasinfo.dto.response.reservation.ReservationResponse;
import co.edu.udea.salasinfo.mapper.request.ReservationRequestMapper;
import co.edu.udea.salasinfo.mapper.response.ReservationResponseMapper;
import co.edu.udea.salasinfo.model.Reservation;
import co.edu.udea.salasinfo.model.ReservationState;
import co.edu.udea.salasinfo.persistence.ReservationDAO;
import co.edu.udea.salasinfo.persistence.ReservationStateDAO;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import co.edu.udea.salasinfo.utils.enums.ReservationType;
import co.edu.udea.salasinfo.utils.enums.WeekDay;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationDAO reservationDAO;

    @Mock
    private ReservationStateDAO reservationStateDAO;

    @Mock
    private ReservationResponseMapper reservationResponseMapper;

    @Mock
    private ReservationRequestMapper reservationRequestMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation mockReservation;
    private ReservationResponse mockReservationResponse;

    @BeforeEach
    void setUp() {
        // Initialize mock objects
        mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setActivityName("Meeting");
        mockReservation.setActivityDescription("Team Meeting");
        mockReservation.setStartsAt(LocalDateTime.now().plusDays(1));
        mockReservation.setEndsAt(LocalDateTime.now().plusDays(1).plusHours(1));
        mockReservation.setReservationState(new ReservationState(1L, RStatus.PENDING));

        mockReservationResponse = new ReservationResponse();
        mockReservationResponse.setId(1L);
        mockReservationResponse.setActivityName("Meeting");
        mockReservationResponse.setActivityDescription("Team Meeting");
        mockReservationResponse.setStartsAt(mockReservation.getStartsAt());
        mockReservationResponse.setEndsAt(mockReservation.getEndsAt());
    }

    @Test
    void findAll_ReturnsListOfReservations() {
        // Arrange
        when(reservationDAO.findAll()).thenReturn(Collections.singletonList(mockReservation));
        when(reservationResponseMapper.toResponses(any())).thenReturn(Collections.singletonList(mockReservationResponse));

        // Act
        List<ReservationResponse> responses = reservationService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(mockReservationResponse, responses.get(0));
    }

    @Test
    void findById_ReturnsReservationResponse() {
        // Arrange
        when(reservationDAO.findById(anyLong())).thenReturn(mockReservation);
        when(reservationResponseMapper.toResponse(any())).thenReturn(mockReservationResponse);

        // Act
        ReservationResponse response = reservationService.findById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(mockReservationResponse, response);
    }

    @Test
    void saveSingleTimeReservation_CreatesOnceReservation() {
        // Arrange
        ReservationRequest request = new ReservationRequest();
        request.setActivityName("Single Meeting");
        request.setActivityDescription("One-time meeting");
        request.setDate(LocalDate.now().plusDays(1));
        request.setStartsAt(LocalTime.now());
        request.setEndsAt(LocalTime.now().plusHours(1));

        mockReservation = new Reservation();
        mockReservation.setType(ReservationType.ONCE);
        when(reservationRequestMapper.toEntity(request)).thenReturn(mockReservation);
        when(reservationDAO.existsByStartsAtAndRoomId(any(), any())).thenReturn(false);
        when(reservationStateDAO.findByState(RStatus.PENDING)).thenReturn(new ReservationState());
        when(reservationDAO.save(any())).thenReturn(mockReservation);
        when(reservationResponseMapper.toResponse(any())).thenReturn(mockReservationResponse);

        // Act
        ReservationResponse response = reservationService.saveSingleTimeReservation(request);

        // Assert
        assertNotNull(response);
        assertEquals(mockReservationResponse, response);
        assertEquals(ReservationType.ONCE, mockReservation.getType());
        verify(reservationDAO).save(mockReservation);
    }

    @Test
    void saveFrequentReservation_CreatesWeeklyReservation() {
        // Arrange
        ReservationRequest request = new ReservationRequest();
        request.setActivityName("Weekly Meeting");
        request.setActivityDescription("Recurring weekly meeting");
        request.setDate(LocalDate.now().plusDays(1));
        request.setStartsAt(LocalTime.now());
        request.setEndsAt(LocalTime.now().plusHours(1));

        mockReservation = new Reservation();
        mockReservation.setType(ReservationType.WEEKLY);
        when(reservationRequestMapper.toEntity(request)).thenReturn(mockReservation);
        when(reservationDAO.existsByStartsAtAndRoomId(any(), any())).thenReturn(false);
        when(reservationStateDAO.findByState(RStatus.PENDING)).thenReturn(new ReservationState());
        when(reservationDAO.save(any())).thenReturn(mockReservation);
        when(reservationResponseMapper.toResponse(any())).thenReturn(mockReservationResponse);

        // Act
        ReservationResponse response = reservationService.saveFrequentReservation(request);

        // Assert
        assertNotNull(response);
        assertEquals(mockReservationResponse, response);
        assertEquals(ReservationType.WEEKLY, mockReservation.getType());
        verify(reservationDAO).save(mockReservation);
    }

    @Test
    void delete_RemovesReservation() {
        // Arrange
        when(reservationDAO.findById(anyLong())).thenReturn(mockReservation);
        doNothing().when(reservationDAO).deleteById(anyLong());
        when(reservationResponseMapper.toResponse(any())).thenReturn(mockReservationResponse);

        // Act
        ReservationResponse response = reservationService.delete(1L);

        // Assert
        assertNotNull(response);
        assertEquals(mockReservationResponse, response);
        verify(reservationDAO).deleteById(1L);
    }

    @Test
    void update_UpdatesExistingReservation() {
        // Arrange
        ReservationRequest request = new ReservationRequest();
        request.setActivityName("Updated Meeting");
        request.setActivityDescription("Updated Description");
        request.setDate(LocalDate.now().plusDays(4));
        request.setStartsAt(LocalTime.now());
        request.setEndsAt(LocalTime.now().plusHours(2));

        when(reservationDAO.findById(anyLong())).thenReturn(mockReservation);
        when(reservationRequestMapper.toEntity(request)).thenReturn(mockReservation);
        when(reservationDAO.save(any())).thenReturn(mockReservation);
        when(reservationResponseMapper.toResponse(any())).thenReturn(mockReservationResponse);

        // Act
        ReservationResponse response = reservationService.update(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals(mockReservationResponse, response);
        verify(reservationDAO).save(any());
    }

    @Test
    void findStated_ReturnsFilteredReservations() {
        // Arrange
        when(reservationDAO.findAll()).thenReturn(Collections.singletonList(mockReservation));
        when(reservationResponseMapper.toResponses(any())).thenReturn(Collections.singletonList(mockReservationResponse));

        // Act
        List<ReservationResponse> responses = reservationService.findStated(RStatus.PENDING);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(mockReservationResponse, responses.get(0));
    }

    @Test
    void saveClass_CreatesClassReservations() {
        // Arrange
        ClassReservationRequest classRequest = new ClassReservationRequest();
        classRequest.setActivityName("Class");
        classRequest.setActivityDescription("Weekly Class");
        classRequest.setSemesterStartsAt(LocalDate.now());
        classRequest.setSemesterEndsAt(LocalDate.now().plusMonths(2));

        // Set up sessions
        SessionRequest session1 = new SessionRequest();
        session1.setDay(WeekDay.MONDAY);
        session1.setStartsAt(LocalTime.of(9, 0));
        session1.setEndsAt(LocalTime.of(11, 0));

        SessionRequest session2 = new SessionRequest();
        session2.setDay(WeekDay.WEDNESDAY);
        session2.setStartsAt(LocalTime.of(14, 0));
        session2.setEndsAt(LocalTime.of(16, 0));

        classRequest.setSessions(Arrays.asList(session1, session2));

        // Mock behavior
        when(reservationRequestMapper.toRequest(classRequest)).thenReturn(new ReservationRequest());
        when(reservationRequestMapper.toEntities(any())).thenReturn(Collections.singletonList(mockReservation));
        when(reservationStateDAO.findByState(RStatus.ACCEPTED)).thenReturn(new ReservationState());
        when(reservationResponseMapper.toResponses(any())).thenReturn(Collections.singletonList(mockReservationResponse));

        // Act
        List<ReservationResponse> responses = reservationService.saveClass(classRequest);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(mockReservationResponse, responses.get(0));
    }

}