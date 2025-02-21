package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Notification;
import co.edu.udea.salasinfo.model.User;
import co.edu.udea.salasinfo.repository.NotificationRepository;
import co.edu.udea.salasinfo.utils.enums.NotificationType;
import co.edu.udea.salasinfo.utils.inner_filters.NotificationInnerFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationJPATest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationJPA notificationJPA;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = Notification.builder()
                .id(1L)
                .user(new User()) // Assuming User class exists
                .message("Test Notification")
                .timestamp(LocalDateTime.now())
                .type(NotificationType.PRIVATE)
                .build();
    }

    @Test
    void findById_ShouldReturnNotification_WhenExists() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Notification found = notificationJPA.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(notificationRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenNotExists() {
        when(notificationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> notificationJPA.findById(2L));
        verify(notificationRepository, times(1)).findById(2L);
    }

    @Test
    void findAll_ShouldReturnFilteredNotifications_WhenFilterApplied() {
        NotificationInnerFilter filter = new NotificationInnerFilter(NotificationType.ADMIN, "123");
        when(notificationRepository.findAll(any(Specification.class))).thenReturn(List.of());

        List<Notification> notifications = notificationJPA.findAll(filter);

        assertNotNull(notifications);
        assertTrue(notifications.isEmpty());
        verify(notificationRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void save_ShouldReturnSavedNotification() {
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification saved = notificationJPA.save(notification);

        assertNotNull(saved);
        assertEquals(notification.getId(), saved.getId());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void deleteById_ShouldDeleteNotification_WhenExists() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        doNothing().when(notificationRepository).delete(notification);

        Notification deleted = notificationJPA.deleteById(1L);

        assertNotNull(deleted);
        assertEquals(1L, deleted.getId());
        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).delete(notification);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotExists() {
        when(notificationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> notificationJPA.deleteById(2L));
        verify(notificationRepository, times(1)).findById(2L);
    }
}
