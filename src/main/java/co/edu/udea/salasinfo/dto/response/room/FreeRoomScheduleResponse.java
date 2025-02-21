package co.edu.udea.salasinfo.dto.response.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FreeRoomScheduleResponse {

    private List<FreeScheduleResponse> freeStartTimes;
    private List<FreeScheduleResponse> freeEndTimes;
}
