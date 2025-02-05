package co.edu.udea.salasinfo.dto.response.room;

import co.edu.udea.salasinfo.dto.response.ImplementResponse;
import co.edu.udea.salasinfo.utils.enums.ImplementCondition;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RoomImplementResponse {
    private Long id;
    private ImplementResponse implement;
    private String state;
}
