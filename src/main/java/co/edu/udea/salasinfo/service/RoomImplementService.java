package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.RoomApplicationRequest;
import co.edu.udea.salasinfo.dto.request.RoomImplementRequest;
import co.edu.udea.salasinfo.dto.response.room.RoomImplementResponse;

public interface RoomImplementService {
    RoomImplementResponse addRoomImplement(Long roomId, RoomImplementRequest request);

}
