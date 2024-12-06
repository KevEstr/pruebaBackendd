package co.edu.udea.salasinfo.mapper.request;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.utils.Generated;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@AnnotateWith(Generated.class)
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomRequestMapper {
    Room toEntity(RoomRequest request);

    List<Room> toEntities(List<RoomRequest> roomRequests);
}
