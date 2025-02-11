package co.edu.udea.salasinfo.dto.request;

import co.edu.udea.salasinfo.utils.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClassReservationRequest {
    @NotNull(message = Constants.ACTIVITY_NAME_FIELD_NOT_NULL_MESSAGE)
    private String activityName;

    @NotNull(message = Constants.ACTIVITY_DESCRIPTION_FIELD_NOT_NULL_MESSAGE)
    private String activityDescription;

    @NotNull(message = Constants.SESSIONS_FIELD_NOT_NULL_MESSAGE)
    @Size(min = 1, max = 3, message = Constants.SESSION_FIELD_OUT_OF_BOUNDS_MESSAGE)
    private List<SessionRequest> sessions;

    @NotNull(message = Constants.USER_ID_FIELD_NOT_NULL_MESSAGE)
    private String userId;

    @NotNull(message = Constants.ROOM_ID_FIELD_NOT_NULL_MESSAGE)
    private Long roomId;

    @NotNull(message = Constants.SEMESTER_STARTS_AT_FIELD_NOT_NULL_MESSAGE)
    private LocalDate semesterStartsAt;

    @NotNull(message = Constants.SEMESTER_ENDS_AT_FIELD_NOT_NULL_MESSAGE)
    private LocalDate semesterEndsAt;
}
