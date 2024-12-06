package co.edu.udea.salasinfo.dto.request;

import co.edu.udea.salasinfo.utils.Constants;
import co.edu.udea.salasinfo.utils.enums.ImplementCondition;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoomRequest {
    @NotNull(message = Constants.COMPUTER_AMOUNT_FIELD_NOT_NULL_MESSAGE)
    @Positive(message = Constants.COMPUTER_AMOUNT_POSITIVE_MESSAGE)
    private Integer computerAmount;
    @NotNull(message = Constants.BUILDING_FIELD_NOT_NULL_MESSAGE)
    private String building;
    @NotNull(message = Constants.ROOM_NUM_FIELD_NOT_NULL_MESSAGE)
    private String roomNum;
    @NotNull(message = Constants.ROOM_NAME_FIELD_NOT_NULL_MESSAGE)
    private String roomName;
    @NotNull(message = Constants.SUB_ROOM_FIELD_NOT_NULL_MESSAGE)
    private Integer subRoom;
    private List<Long> implementIds;
    @Valid
    @Enumerated(EnumType.STRING)
    private List<ImplementCondition> implementStates;

    private List<Long> softwareIds;
    private List<String> softwareVersions;

    private List<Long> restrictionIds;
}
