package co.edu.udea.salasinfo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomApplicationRequest {
    @NotNull
    private Long roomId;

    @NotNull
    private Long applicationId;

    @NotNull
    private String version;
}
