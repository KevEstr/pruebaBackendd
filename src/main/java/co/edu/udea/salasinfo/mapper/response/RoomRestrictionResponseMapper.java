package co.edu.udea.salasinfo.mapper.response;

import co.edu.udea.salasinfo.dto.response.room.RoomRestrictionResponse;
import co.edu.udea.salasinfo.model.RoomRestriction;
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
public interface RoomRestrictionResponseMapper {

    @Mapping(source = "restriction", target = "restriction")
    RoomRestrictionResponse toResponse(RoomRestriction roomRestriction);

    List<RoomRestrictionResponse> toResponses(List<RoomRestriction> roomRestriction);

}
