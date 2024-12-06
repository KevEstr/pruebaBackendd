package co.edu.udea.salasinfo.dto.response.room;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class FreeScheduleResponse {

    // Getter y Setter
    private LocalTime hour;

    public FreeScheduleResponse(LocalTime hour) {
        this.hour = hour;
    }

}
