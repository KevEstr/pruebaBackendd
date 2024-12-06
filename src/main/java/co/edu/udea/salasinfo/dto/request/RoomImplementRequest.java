package co.edu.udea.salasinfo.dto.request;

import co.edu.udea.salasinfo.utils.enums.ImplementCondition;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomImplementRequest {
    @NotNull
    private Long roomId;

    @NotNull
    private Long implementId;

    @NotNull
    private ImplementCondition state;
}
