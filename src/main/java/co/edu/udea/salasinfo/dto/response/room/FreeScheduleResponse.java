package co.edu.udea.salasinfo.dto.response.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FreeScheduleResponse {

    private String hour;
}
