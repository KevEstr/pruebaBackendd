package co.edu.udea.salasinfo.controller.v1;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.ClassReservationRequest;
import co.edu.udea.salasinfo.dto.request.ReservationRequest;
import co.edu.udea.salasinfo.dto.request.SessionRequest;
import co.edu.udea.salasinfo.dto.response.reservation.ReservationResponse;
import co.edu.udea.salasinfo.dto.response.reservation.ReservationUserResponse;
import co.edu.udea.salasinfo.service.ReservationService;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import co.edu.udea.salasinfo.utils.enums.ReservationType;
import co.edu.udea.salasinfo.utils.enums.WeekDay;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    JwtService jwtService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSaveReservation_Success() throws Exception {
        // Arrange
        ReservationRequest request = new ReservationRequest(
                "Yoga Class",
                "Relaxing yoga session",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                ReservationType.WEEKLY,
                "user123",
                1L
        );
        ReservationResponse response = new ReservationResponse(1L, "Yoga Class", "Relaxing yoga session",
                LocalDateTime.now().plusDays(20), LocalDateTime.now().plusDays(20),
                ReservationType.WEEKLY, null, null, null);

        when(reservationService.save(any(ReservationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.activityName").value("Yoga Class"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSaveReservation_BadRequest() throws Exception {
        // Arrange
        ReservationRequest request = new ReservationRequest(null, null, null, null, null, null, null);

        // Act & Assert
        mockMvc.perform(post("/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateClass_Success() throws Exception {
        // Arrange
        ClassReservationRequest request = new ClassReservationRequest(
                "Group Fitness",
                "High-energy group class",
                List.of(
                        new SessionRequest(WeekDay.SATURDAY, LocalTime.of(12, 0), LocalTime.of(14, 0))
                ),
                "user123",
                1L,
                LocalDate.now(),
                LocalDate.now().plusMonths(1)
        );

        ReservationResponse response = new ReservationResponse(2L, "Group Fitness", "High-energy group class",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                ReservationType.WEEKLY, new ReservationUserResponse("user123", "name", "lastname", "email"), null, null);

        when(reservationService.saveClass(any(ClassReservationRequest.class))).thenReturn(Collections.singletonList(response));

        // Act & Assert
        mockMvc.perform(post("/v1/reservations/class")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].activityName").value("Group Fitness"));
    }

    @Test
    void testFindAll_Success() throws Exception {
        // Arrange
        ReservationResponse response1 = new ReservationResponse(1L, "Yoga Class", "Relaxing yoga session",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), ReservationType.WEEKLY, null, null, null);
        ReservationResponse response2 = new ReservationResponse(2L, "Group Fitness", "High-energy group class",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), ReservationType.WEEKLY, null, null, null);

        when(reservationService.findAll()).thenReturn(Arrays.asList(response1, response2));

        // Act & Assert
        mockMvc.perform(get("/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindRefused_Success() throws Exception {
        // Arrange
        ReservationResponse response = new ReservationResponse(3L, "Canceled Reservation", "This was canceled",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), ReservationType.WEEKLY, null, null, null);

        when(reservationService.findStated(RStatus.REJECTED)).thenReturn(Collections.singletonList(response));

        // Act & Assert
        mockMvc.perform(get("/v1/reservations/rejected")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindAccepted() throws Exception {
        // Arrange
        ReservationResponse response = new ReservationResponse(3L, "Canceled Reservation", "This was canceled",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), ReservationType.WEEKLY, null, null, null);

        when(reservationService.findStated(RStatus.ACCEPTED)).thenReturn(Collections.singletonList(response));

        // Act & Assert
        mockMvc.perform(get("/v1/reservations/accepted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindPending() throws Exception {
        // Arrange
        ReservationResponse response = new ReservationResponse(3L, "Canceled Reservation", "This was canceled",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), ReservationType.WEEKLY, null, null, null);

        when(reservationService.findStated(RStatus.PENDING)).thenReturn(Collections.singletonList(response));

        // Act & Assert
        mockMvc.perform(get("/v1/reservations/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAcceptReservation_Success() throws Exception {
        // Arrange
        ReservationResponse response = new ReservationResponse(1L, "Yoga Class", "Relaxing yoga session",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), ReservationType.WEEKLY, null, null, null);

        when(reservationService.updateState(1L, RStatus.ACCEPTED)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/v1/reservations/1/accept")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRejectReservation_Success() throws Exception {
        // Arrange
        ReservationResponse response = new ReservationResponse(2L, "Yoga Class", "Relaxing yoga session",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), ReservationType.WEEKLY, null, null, null);

        when(reservationService.updateState(2L, RStatus.REJECTED)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/v1/reservations/2/reject")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2L));
    }
}