package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.SpecificRoomResponse;
import co.edu.udea.salasinfo.mapper.request.RoomRequestMapper;
import co.edu.udea.salasinfo.mapper.response.RoomResponseMapper;
import co.edu.udea.salasinfo.mapper.response.RoomScheduleResponseMapper;
import co.edu.udea.salasinfo.mapper.response.SpecificRoomResponseMapper;
import co.edu.udea.salasinfo.model.Reservation;
import co.edu.udea.salasinfo.persistence.ReservationDAO;
import co.edu.udea.salasinfo.persistence.RoomDAO;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.service.RoomService;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * It's the rooms data accessor, which saves and retrieves rooms
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomDAO roomDAO;
    private final ReservationDAO reservationDAO;
    private final RoomResponseMapper roomResponseMapper;
    private final RoomRequestMapper roomRequestMapper;
    private final SpecificRoomResponseMapper specificRoomResponseMapper;
    private final RoomScheduleResponseMapper roomScheduleResponseMapper;

    /**
     * Retrieves a List of Rooms from the Database
     *
     * @return A list of the retrieved Rooms.
     */
    public List<RoomResponse> findAll(RoomFilter filter) {
        List<Room> rooms = roomDAO.findAll(filter);
        return roomResponseMapper.toResponses(rooms);
    }

    /**
     * Seeks for a Room in the database given a specific id.
     *
     * @param id The id of the Wanted Room.
     * @return A Response Entity with the found room as body and status code 200
     * Or a Response Entity without body and status code 404.
     */
    public SpecificRoomResponse findById(Long id) {
        Room optRoom = roomDAO.findById(id);
        return specificRoomResponseMapper.toResponse(optRoom);
    }


    /**
     * Receives a room, creates the room's id using the building, number and sub room, then save it in the database.
     *
     * @param room A room object with the needed attributes.
     * @return A response entity with a 200 as status code and the saved room as body if there's a success and a 400 if something is wrong.
     */
    public RoomResponse createRoom(RoomRequest room) {
        String stringId = room.getBuilding() +
                room.getRoomNum() +
                room.getSubRoom();
        Long id = Long.parseLong(stringId);
        try {
            roomDAO.findById(id);
            throw new EntityExistsException("Room with id " + id + " already exists");
        }catch (EntityExistsException e) {
            Room entity = roomRequestMapper.toEntity(room);
            entity.setId(id);
            return roomResponseMapper.toResponse(roomDAO.save(entity));
        }
    }

    /**
     * Updates the room of the given room's id with using the changes of the given room object.
     *
     * @param room The room that contain the changes.
     * @return A response entity with 200 as status code and the updated room as body
     * or a response entity with 404 as status code.
     */
    public RoomResponse updateRoom(Long id, RoomRequest room) {
        Room foundRoom = roomDAO.findById(id);
        if (room.getRoomName() != null) foundRoom.setRoomName(room.getRoomName());
        if (room.getComputerAmount() != null) foundRoom.setComputerAmount(room.getComputerAmount());
        roomDAO.save(foundRoom);
        return roomResponseMapper.toResponse(foundRoom);
    }

    /**
     * Deletes the room of the given id if it exists.
     *
     * @param id The id of the room that is going to be deleted.
     * @return The Deleted room.
     */
    public RoomResponse deleteRoom(Long id) {
        Room deletedRoom = roomDAO.findById(id);
        roomDAO.deleteById(id);
        return roomResponseMapper.toResponse(deletedRoom);
    }

    /**
     * Find free room at the given time.
     * <p>
     * It filters the accepted reservations, filters occupied rooms at that time and then gets occupied rooms.
     * With those rooms it filters free rooms.
     * @param date a date that must be between [startAt, endsAt)
     * @return A list of free rooms at the given time.
     */
    @Override
    public List<RoomResponse> findFreeAt(LocalDateTime date) {
        List<Reservation> reservations = reservationDAO.findAll();
        List<Room> occupiedReservations = reservations.stream()
                .filter(reservation -> reservation.getReservationState().getState() == RStatus.ACCEPTED) // Accepted reservations
                .filter(reservation -> { // Reservations at that specific time
                    if(reservation.getStartsAt().isEqual(date)) return true;
                    return date.isAfter(reservation.getStartsAt()) && date.isBefore(reservation.getEndsAt());
                })
                .map(Reservation::getRoom) // Occupied room
                .toList();
        List<Room> freeRooms = roomDAO.findAll(null).stream()
                .filter(room -> !occupiedReservations.contains(room)).toList();
        return roomResponseMapper.toResponses(freeRooms);
    }

    @Override
    public List<RoomScheduleResponse> findRoomSchedule(Long id) {
        Room foundRoom = roomDAO.findById(id);
        List<Reservation> reservations = foundRoom.getReservations().stream()
                .filter(reservation -> reservation.getReservationState().getState().equals(RStatus.ACCEPTED))
                .toList();
        return roomScheduleResponseMapper.toResponses(reservations);
    }
}
