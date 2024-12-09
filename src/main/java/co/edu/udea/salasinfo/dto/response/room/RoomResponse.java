package co.edu.udea.salasinfo.dto.response.room;

import co.edu.udea.salasinfo.model.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoomResponse {
    private Integer id;
    private Integer computerAmount;
    private String building;
    private String roomNum;
    private String roomName;
    private Integer subRoom;
    private List<RoomApplicationResponse> software;
    private List<RoomRestrictionResponse> restrictions;
    private List<RoomImplementResponse> implementsList;
}
