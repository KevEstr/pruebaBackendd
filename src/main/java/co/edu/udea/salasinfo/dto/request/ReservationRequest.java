package co.edu.udea.salasinfo.dto.request;

import co.edu.udea.salasinfo.utils.Constants;
import co.edu.udea.salasinfo.utils.enums.ReservationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReservationRequest {
    @NotNull(message = Constants.ACTIVITY_NAME_FIELD_NOT_NULL_MESSAGE)
    private String activityName;

    @NotNull(message = Constants.ACTIVITY_DESCRIPTION_FIELD_NOT_NULL_MESSAGE)
    private String activityDescription;

    @NotNull(message = Constants.DATE_FIELD_NOT_NULL_MESSAGE)
    @Future(message = Constants.DATE_FIELD_NOT_PAST_MESSAGE)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private LocalDate date;

    @NotNull(message = Constants.STARTS_AT_FIELD_NOT_NULL_MESSAGE)
    @Schema(type = "String", pattern = Constants.HOUR_FORMAT)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.HOUR_FORMAT)
    private LocalTime startsAt;

    @NotNull(message = Constants.ENDS_AT_FIELD_NOT_NULL_MESSAGE)
    @Schema(type = "String", pattern = Constants.HOUR_FORMAT)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.HOUR_FORMAT)
    private LocalTime endsAt;

    @NotNull(message = Constants.USER_ID_FIELD_NOT_NULL_MESSAGE)
    private String userId;

    @NotNull(message = Constants.ROOM_ID_FIELD_NOT_NULL_MESSAGE)
    private Long roomId;
}
