package co.edu.udea.salasinfo.dto.response;

import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RestrictionResponse {
    // Attributes
    private Long id;
    private String description;
    //private List<RoomResponse> rooms;
}
