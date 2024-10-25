package co.edu.udea.salasinfo.dto.response.room;

import co.edu.udea.salasinfo.utils.Constants;
import co.edu.udea.salasinfo.utils.enums.ReservationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomScheduleResponse {
    private Long id;
    private String activityName;
    private String activityDescription;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_FORMAT)
    private LocalDateTime startsAt;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_FORMAT)
    private LocalDateTime endsAt;
    private ReservationType type;
}
