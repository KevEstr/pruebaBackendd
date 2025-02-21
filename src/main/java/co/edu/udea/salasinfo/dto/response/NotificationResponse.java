package co.edu.udea.salasinfo.dto.response;

import co.edu.udea.salasinfo.utils.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationResponse {
    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private NotificationType type;
}
