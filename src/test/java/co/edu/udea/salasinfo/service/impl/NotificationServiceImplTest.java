package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.NotificationRequest;
import co.edu.udea.salasinfo.dto.response.NotificationResponse;
import co.edu.udea.salasinfo.mapper.request.NotificationRequestMapper;
import co.edu.udea.salasinfo.mapper.response.NotificationResponseMapper;
import co.edu.udea.salasinfo.model.Notification;
import co.edu.udea.salasinfo.model.Role;
import co.edu.udea.salasinfo.model.User;
import co.edu.udea.salasinfo.persistence.NotificationDAO;
import co.edu.udea.salasinfo.persistence.UserDAO;
import co.edu.udea.salasinfo.utils.enums.NotificationType;
import co.edu.udea.salasinfo.utils.enums.RoleName;
import co.edu.udea.salasinfo.utils.inner_filters.NotificationInnerFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    private static final Role USER_ROLE = Role.builder()
            .id(1L)
            .roleName(RoleName.Usuario)
            .build();
    private static final String NOTIFICATION_MESSAGE = "Notification Message";
    private static final String USER_ID = "user123";
    private static final Long NOTIFICATION_ID = 1L;

    @Mock
    private NotificationDAO notificationDAO;

    @Mock
    private NotificationResponseMapper responseMapper;

    @Mock
    private NotificationRequestMapper requestMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification mockNotification;
    private NotificationResponse mockNotificationResponse;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(USER_ID);
        mockUser.setRole(USER_ROLE);

        mockNotification = new Notification();
        mockNotification.setId(NOTIFICATION_ID);
        mockNotification.setMessage(NOTIFICATION_MESSAGE);
        mockNotification.setTimestamp(LocalDateTime.now());
        mockNotification.setType(NotificationType.PRIVATE);
        mockNotification.setUser(mockUser);

        mockNotificationResponse = new NotificationResponse();
        mockNotificationResponse.setId(NOTIFICATION_ID);
        mockNotificationResponse.setMessage(NOTIFICATION_MESSAGE);
        mockNotificationResponse.setTimestamp(mockNotification.getTimestamp());
        mockNotificationResponse.setType(NotificationType.PRIVATE);
    }

    @Test
    void findById_ReturnsNotificationResponse() {
        // Arrange
        when(notificationDAO.findById(anyLong())).thenReturn(mockNotification);
        when(responseMapper.toResponse(any())).thenReturn(mockNotificationResponse);

        // Act
        NotificationResponse response = notificationService.findById(NOTIFICATION_ID);

        // Assert
        assertNotNull(response);
        assertEquals(mockNotificationResponse, response);
        verify(notificationDAO).findById(NOTIFICATION_ID);
    }

    @Test
    void findAll_ReturnsListOfNotifications() {
        // Arrange
        when(jwtService.extractUsername(any())).thenReturn(USER_ID);
        when(userDAO.findById(any())).thenReturn(mockUser);
        when(notificationDAO.findAll(any())).thenReturn(Collections.singletonList(mockNotification));
        when(responseMapper.toResponses(any())).thenReturn(Collections.singletonList(mockNotificationResponse));

        // Act
        List<NotificationResponse> responses = notificationService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(mockNotificationResponse, responses.get(0));
        verify(notificationDAO).findAll(any(NotificationInnerFilter.class));
    }

    @Test
    void save_CreatesNewNotification() {
        // Arrange
        NotificationRequest request = new NotificationRequest();
        request.setReceiverId(USER_ID);
        request.setMessage(NOTIFICATION_MESSAGE);
        request.setTimestamp(LocalDateTime.now());
        request.setType(NotificationType.PRIVATE);

        when(requestMapper.toEntity(any())).thenReturn(mockNotification);
        when(userDAO.findById(any())).thenReturn(mockUser);
        when(notificationDAO.save(any())).thenReturn(mockNotification);
        when(responseMapper.toResponse(any())).thenReturn(mockNotificationResponse);

        // Act
        NotificationResponse response = notificationService.save(request);

        // Assert
        assertNotNull(response);
        assertEquals(mockNotificationResponse, response);
        verify(notificationDAO).save(any(Notification.class));
    }

    @Test
    void deleteById_RemovesNotification() {
        // Arrange
        when(notificationDAO.deleteById(anyLong())).thenReturn(mockNotification);
        when(responseMapper.toResponse(any())).thenReturn(mockNotificationResponse);

        // Act
        NotificationResponse response = notificationService.deleteById(NOTIFICATION_ID);

        // Assert
        assertNotNull(response);
        assertEquals(mockNotificationResponse, response);
        verify(notificationDAO).deleteById(NOTIFICATION_ID);
    }
}
