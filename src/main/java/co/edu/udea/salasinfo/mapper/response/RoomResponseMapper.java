package co.edu.udea.salasinfo.mapper.response;

import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.utils.Generated;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@AnnotateWith(Generated.class)
@Mapper(componentModel = "spring",
        uses = RoomApplicationResponseMapper.class,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomResponseMapper {

    @Mapping(source = "roomApplications", target = "software")
    @Mapping(source = "restrictions", target = "restrictions")
    @Mapping(source = "implementList", target = "implementsList")
    RoomResponse toResponse(Room room);

    List<RoomResponse> toResponses(List<Room> rooms);
}
