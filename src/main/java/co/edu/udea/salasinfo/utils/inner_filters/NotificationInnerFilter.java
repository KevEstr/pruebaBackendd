package co.edu.udea.salasinfo.utils.inner_filters;

// Why does inner_filter class exist?
// As Filters already exist but are DTOs,
// an inner filter was needed to not expose filtering processes of notification service

import co.edu.udea.salasinfo.utils.enums.NotificationType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationInnerFilter {
    private NotificationType type;
    private String userId;
}
