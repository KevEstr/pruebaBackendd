package co.edu.udea.salasinfo.service;


import co.edu.udea.salasinfo.dto.request.RoomApplicationRequest;
import co.edu.udea.salasinfo.dto.response.room.RoomApplicationResponse;

public interface RoomApplicationService {
    RoomApplicationResponse addRoomApplication(Long roomId, RoomApplicationRequest request);
}
