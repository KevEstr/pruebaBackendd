package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.dto.response.room.FreeScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.SpecificRoomResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * It's the rooms data accessor, which saves and retrieves rooms
 */
public interface RoomService {
    List<RoomResponse> findAll(RoomFilter filter);
    SpecificRoomResponse findById(Long id) ;
    RoomResponse createRoom(RoomRequest room) ;
    RoomResponse updateRoom(Long id, RoomRequest room) ;
    RoomResponse deleteRoom(Long id) ;
    List<RoomResponse> findFreeAt(LocalDateTime date);
    List<RoomScheduleResponse> findRoomSchedule(Long id);

    List<FreeScheduleResponse> findFreeRoomSchedule(Long id, LocalDate selectedDate);
}
