package co.edu.udea.salasinfo.dto.request;

import co.edu.udea.salasinfo.utils.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationRequest {
    private String receiverId;
    private String message;
    private LocalDateTime timestamp;
    private NotificationType type;
}