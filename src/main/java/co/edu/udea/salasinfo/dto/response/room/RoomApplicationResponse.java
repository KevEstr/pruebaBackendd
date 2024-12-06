package co.edu.udea.salasinfo.dto.response.room;

import co.edu.udea.salasinfo.dto.response.ApplicationResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RoomApplicationResponse {
    private Long id;
    private ApplicationResponse application;
    private String version;
}
