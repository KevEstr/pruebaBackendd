package co.edu.udea.salasinfo.mapper.response;

import co.edu.udea.salasinfo.dto.response.room.RoomApplicationResponse;
import co.edu.udea.salasinfo.model.RoomApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomApplicationResponseMapper {
    @Mapping(source = "version", target = "version")
    @Mapping(source = "application", target = "application")
    RoomApplicationResponse toResponse(RoomApplication roomApplication);

    List<RoomApplicationResponse> toResponses(List<RoomApplication> roomApplications);
}
