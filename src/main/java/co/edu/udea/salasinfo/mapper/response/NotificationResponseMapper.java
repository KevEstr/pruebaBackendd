package co.edu.udea.salasinfo.mapper.response;

import co.edu.udea.salasinfo.dto.response.NotificationResponse;
import co.edu.udea.salasinfo.model.Notification;
import co.edu.udea.salasinfo.utils.Generated;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@AnnotateWith(Generated.class)
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationResponseMapper {
    NotificationResponse toResponse(Notification notification);
    List<NotificationResponse> toResponses(List<Notification> notifications);
}
