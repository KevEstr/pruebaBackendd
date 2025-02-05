package co.edu.udea.salasinfo.dto.response.room;

import co.edu.udea.salasinfo.dto.response.RestrictionResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoomRestrictionResponse {
    // Attributes
    private Integer id;
    private RestrictionResponse restriction;
}
