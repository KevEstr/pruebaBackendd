package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.response.NotificationResponse;
import co.edu.udea.salasinfo.service.NotificationService;
import co.edu.udea.salasinfo.utils.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    JwtService jwtService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnListOfNotifications() throws Exception {
        List<NotificationResponse> notifications = Arrays.asList(
                new NotificationResponse(1L, "New update available", LocalDateTime.now(), NotificationType.ADMIN),
                new NotificationResponse(2L, "Meeting at 3 PM", LocalDateTime.now(), NotificationType.PRIVATE)
        );

        when(notificationService.findAll()).thenReturn(notifications);

        mockMvc.perform(get("/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].message").value("New update available"))
                .andExpect(jsonPath("$[0].type").value("ADMIN"));
    }

    @Test
    @WithMockUser(roles = "Admin")
    void deleteById_ShouldDeleteNotification() throws Exception {
        NotificationResponse response = new NotificationResponse(1L, "New update available", LocalDateTime.now(), NotificationType.ADMIN);

        when(notificationService.deleteById(1L)).thenReturn(response);

        mockMvc.perform(delete("/v1/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("New update available"));
    }
}
