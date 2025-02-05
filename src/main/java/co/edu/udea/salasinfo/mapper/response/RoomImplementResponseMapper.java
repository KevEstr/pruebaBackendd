package co.edu.udea.salasinfo.mapper.response;

import co.edu.udea.salasinfo.dto.response.room.RoomImplementResponse;
import co.edu.udea.salasinfo.model.RoomImplement;
import co.edu.udea.salasinfo.utils.Generated;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@AnnotateWith(Generated.class)
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomImplementResponseMapper {

    @Mapping(source = "implement", target = "implement")
    @Mapping(source = "state", target = "state")
    RoomImplementResponse toResponse(RoomImplement roomImplement);

    List<RoomImplementResponse> toResponses(List<RoomImplement> roomImplements);
}
