package co.edu.udea.salasinfo.dto.response.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SpecificRoomResponse {
    private Integer id;
    private Integer computerAmount;
    private String building;
    private String roomNum;
    private String roomName;
    private Integer subRoom;
    private List<RoomApplicationResponse> software;
    private List<RoomRestrictionResponse> restrictions;
    @JsonProperty("implements")
    private List<RoomImplementResponse> implementList;

}
