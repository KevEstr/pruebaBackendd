package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.dto.response.room.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * It's the rooms data accessor, which saves and retrieves rooms
 */
public interface RoomService {
    List<RoomResponse> findAll(RoomFilter filter);
    SpecificRoomResponse findById(Long id) ;
    SpecificRoomResponse createRoom(RoomRequest room) ;
    SpecificRoomResponse updateRoom(Long id, RoomRequest room) ;
    RoomResponse deleteRoom(Long id) ;
    List<RoomResponse> findFreeAt(LocalDateTime start, LocalDateTime end);
    List<RoomScheduleResponse> findRoomSchedule(Long id);
    List<FreeScheduleResponse> findAvailableEndTimes(Long id, LocalDate selectedDate, LocalTime selectedStartTime);
    List<FreeScheduleResponse> findAvailableStartTimes(Long id, LocalDate selectedDate);
}
