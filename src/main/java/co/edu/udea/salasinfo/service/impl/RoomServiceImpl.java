package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.dto.response.room.FreeScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.SpecificRoomResponse;
import co.edu.udea.salasinfo.mapper.request.RoomRequestMapper;
import co.edu.udea.salasinfo.mapper.response.RoomImplementResponseMapper;
import co.edu.udea.salasinfo.mapper.response.RoomResponseMapper;
import co.edu.udea.salasinfo.mapper.response.RoomScheduleResponseMapper;
import co.edu.udea.salasinfo.mapper.response.SpecificRoomResponseMapper;
import co.edu.udea.salasinfo.model.*;
import co.edu.udea.salasinfo.persistence.*;
import co.edu.udea.salasinfo.service.RoomService;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ImplementDAO implementDAO;
    private final ApplicationDAO applicationDAO;
    private final RestrictionDAO restrictionDAO;
    private final RoomImplementDAO roomImplementDAO;
    private final RoomApplicationDAO roomApplicationDAO;
    private final RoomRestrictionDAO roomRestrictionDAO;

    @Autowired
    private RoomImplementResponseMapper roomImplementResponseMapper;

    /**
     * Retrieves a List of Rooms from the Database
     *
     * @return A list of the retrieved Rooms.
     */
    @Override
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
    @Override
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
    @Override
    @Transactional
    public SpecificRoomResponse createRoom(RoomRequest roomRequest) {
        String stringId = roomRequest.getBuilding() + roomRequest.getRoomNum() + roomRequest.getSubRoom();
        Long id = Long.parseLong(stringId);

        if (roomDAO.existsById(id)) {
            throw new IllegalArgumentException("A room with ID " + id + " already exists.");
        }

        // Mapear la solicitud a la entidad Room
        Room room = roomRequestMapper.toEntity(roomRequest);
        room.setId(id);

        // Guardar la sala
        room = roomDAO.save(room);

        // Guardar implementos con estado
        if (roomRequest.getImplementIds() != null && roomRequest.getImplementStates() != null) {
            List<RoomImplement> roomImplementList = new ArrayList<>();
            for (int i = 0; i < roomRequest.getImplementIds().size(); i++) {
                Implement implement = implementDAO.findById(roomRequest.getImplementIds().get(i));
                if (implement == null) {
                    throw new EntityNotFoundException("Implement not found with ID " + roomRequest.getImplementIds().get(i));
                }

                RoomImplement roomImplement = RoomImplement.builder()
                        .room(room)  // Usamos la sala ya persistida
                        .implement(implement)
                        .state(String.valueOf(roomRequest.getImplementStates().get(i)))
                        .build();
                roomImplementDAO.save(roomImplement);
                roomImplementList.add(roomImplement);
            }
            room.setImplementList(roomImplementList);
        }

        // Guardar software con versión
        if (roomRequest.getSoftwareIds() != null && roomRequest.getSoftwareVersions() != null) {
            List<RoomApplication> roomApplicationList = new ArrayList<>();
            for (int i = 0; i < roomRequest.getSoftwareIds().size(); i++) {
                Application application = applicationDAO.findById(roomRequest.getSoftwareIds().get(i));
                if (application == null) {
                    throw new EntityNotFoundException("Application not found with ID " + roomRequest.getSoftwareIds().get(i));
                }

                RoomApplication roomApplication = RoomApplication.builder()
                        .room(room)  // Usamos la sala ya persistida
                        .application(application)
                        .version(roomRequest.getSoftwareVersions().get(i))
                        .build();
                roomApplicationDAO.save(roomApplication);
                roomApplicationList.add(roomApplication);
            }
            room.setRoomApplications(roomApplicationList);
        }

        // Guardar restricciones
        if (roomRequest.getRestrictionIds() != null) {
            List<Restriction> restrictionsList = restrictionDAO.findAllById(roomRequest.getRestrictionIds());
            Room finalRoom = room;
            room.setRestrictions(restrictionsList.stream()
                    .map(restriction -> {
                        RoomRestriction roomRestriction = new RoomRestriction();
                        roomRestriction.setRoom(finalRoom);
                        roomRestriction.setRestriction(restriction);
                        return roomRestriction;
                    })
                    .collect(Collectors.toList()));
        }

        // Refrescar la entidad Room para cargar relaciones
        room = roomDAO.save(room); // We save the room after associating with implements, software, and restrictions.

        // Usamos findById y verificamos la existencia manualmente
        room = roomDAO.findById(room.getId());
        if (room == null) {
            throw new EntityNotFoundException("Room not found");
        }

        return specificRoomResponseMapper.toResponse(room);
    }

    /**
     * Updates the room of the given room's id with using the changes of the given room object.
     *
     * @param room The room that contain the changes.
     * @return A response entity with 200 as status code and the updated room as body
     * or a response entity with 404 as status code.
     */
    @Override
    @Transactional
    public SpecificRoomResponse updateRoom(Long id, RoomRequest roomRequest) {
        // Buscar la sala existente
        Room foundRoom = roomDAO.findById(id);
        if (foundRoom == null) {
            throw new EntityNotFoundException("Room not found with ID " + id);
        }

        // Actualizar los campos básicos si están presentes
        if (roomRequest.getRoomName() != null) {
            foundRoom.setRoomName(roomRequest.getRoomName());
        }
        if (roomRequest.getComputerAmount() != null) {
            foundRoom.setComputerAmount(roomRequest.getComputerAmount());
        }

        // Eliminar implementos antiguos
        if (foundRoom.getImplementList() != null) {
            roomImplementDAO.deleteAll(foundRoom.getImplementList());
        }

        // Actualizar implementos con estado
        if (roomRequest.getImplementIds() != null && roomRequest.getImplementStates() != null) {
            List<RoomImplement> roomImplementList = new ArrayList<>();
            for (int i = 0; i < roomRequest.getImplementIds().size(); i++) {
                Implement implement = implementDAO.findById(roomRequest.getImplementIds().get(i));
                if (implement == null) {
                    throw new EntityNotFoundException("Implement not found with ID " + roomRequest.getImplementIds().get(i));
                }

                RoomImplement roomImplement = RoomImplement.builder()
                        .room(foundRoom)
                        .implement(implement)
                        .state(String.valueOf(roomRequest.getImplementStates().get(i)))
                        .build();
                roomImplementDAO.save(roomImplement);
                roomImplementList.add(roomImplement);
            }
            foundRoom.setImplementList(roomImplementList);
        }

        // Eliminar software antiguo
        if (foundRoom.getRoomApplications() != null) {
            roomApplicationDAO.deleteAll(foundRoom.getRoomApplications());
        }

        // Actualizar software con versión
        if (roomRequest.getSoftwareIds() != null && roomRequest.getSoftwareVersions() != null) {
            List<RoomApplication> roomApplicationList = new ArrayList<>();
            for (int i = 0; i < roomRequest.getSoftwareIds().size(); i++) {
                Application application = applicationDAO.findById(roomRequest.getSoftwareIds().get(i));
                if (application == null) {
                    throw new EntityNotFoundException("Application not found with ID " + roomRequest.getSoftwareIds().get(i));
                }

                RoomApplication roomApplication = RoomApplication.builder()
                        .room(foundRoom)
                        .application(application)
                        .version(roomRequest.getSoftwareVersions().get(i))
                        .build();
                roomApplicationDAO.save(roomApplication);
                roomApplicationList.add(roomApplication);
            }
            foundRoom.setRoomApplications(roomApplicationList);
        }

        // Eliminar restricciones antiguas
        roomRestrictionDAO.deleteAllByRoomId(foundRoom.getId());
        foundRoom.getRestrictions().clear();

        // Actualizar restricciones
        if (roomRequest.getRestrictionIds() != null) {
            List<Restriction> restrictionsList = restrictionDAO.findAllById(roomRequest.getRestrictionIds());
            foundRoom.setRestrictions(restrictionsList.stream()
                    .map(restriction -> {
                        RoomRestriction roomRestriction = new RoomRestriction();
                        roomRestriction.setRoom(foundRoom);
                        roomRestriction.setRestriction(restriction);
                        return roomRestriction;
                    })
                    .collect(Collectors.toList()));
        }

        // Guardar los cambios
        roomDAO.save(foundRoom);
        Room updatedRoom = roomDAO.findById(foundRoom.getId());

        // Devolver la respuesta en el formato esperado
        return specificRoomResponseMapper.toResponse(updatedRoom);
    }



    /**
     * Deletes the room of the given id if it exists.
     *
     * @param id The id of the room that is going to be deleted.
     * @return The Deleted room.
     */
    @Override
    @Transactional
    public RoomResponse deleteRoom(Long id) {
        Room room = roomDAO.findById(id);

        // Verificar si hay reservas
        if (!room.getReservations().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la sala porque tiene reservas asociadas");
        }

        // Limpiar relaciones con implementos
        if (room.getImplementList() != null && !room.getImplementList().isEmpty()) {
            roomImplementDAO.deleteAllByRoomId(room.getId()); // Borrar implementos relacionados
        }

        // Limpiar relaciones con software
        if (room.getRoomApplications() != null && !room.getRoomApplications().isEmpty()) {
            roomApplicationDAO.deleteAllByRoomId(room.getId()); // Borrar software relacionado
        }

        // Limpiar relaciones con restricciones
        if (room.getRestrictions() != null && !room.getRestrictions().isEmpty()) {
            roomRestrictionDAO.deleteAllByRoomId(room.getId()); // Borrar restricciones relacionadas
        }

        roomDAO.deleteById(id);

        return roomResponseMapper.toResponse(room);
    }

    /**
     * Find free room at the given time.
     * <p>
     * It filters the accepted reservations, filters occupied rooms at that time and then gets occupied rooms.
     * With those rooms it filters free rooms.
     *
     * @param date a date that must be between [startAt, endsAt)
     * @return A list of free rooms at the given time.
     */
    @Override
    public List<RoomResponse> findFreeAt(LocalDateTime date) {
        List<Reservation> reservations = reservationDAO.findAll();
        List<Room> occupiedReservations = reservations.stream()
                .filter(reservation -> reservation.getReservationState().getState() == RStatus.ACCEPTED) // Accepted reservations
                .filter(reservation -> { // Reservations at that specific time
                    if (reservation.getStartsAt().isEqual(date)) return true;
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


    @Override
    public List<FreeScheduleResponse> findFreeRoomSchedule(Long id, LocalDate selectedDate) {
        Room foundRoom = roomDAO.findById(id);

        // Obtener las reservas aceptadas para la fecha
        List<Reservation> reservations = foundRoom.getReservations().stream()
                .filter(reservation -> reservation.getReservationState().getState().equals(RStatus.ACCEPTED))
                .filter(reservation -> isSameDay(reservation.getStartsAt(), selectedDate))
                .toList();

        // Generar horario base
        List<LocalTime> availableHours = generateDailySchedule();

        // Excluir horas reservadas
        for (Reservation reservation : reservations) {
            LocalTime reservationStart = reservation.getStartsAt().toLocalTime();
            LocalTime reservationEnd = reservation.getEndsAt().toLocalTime();

            // Remover todas las horas que caen dentro de la reserva
            availableHours.removeIf(hour ->
                    !hour.isBefore(reservationStart) && hour.isBefore(reservationEnd));
        }

        // Formatear las horas disponibles a "H:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return availableHours.stream()
                .map(FreeScheduleResponse::new)
                .toList();
    }

    // Método auxiliar para verificar si la reserva es en la misma fecha seleccionada
    private boolean isSameDay(LocalDateTime dateTime, LocalDate selectedDate) {
        return dateTime.toLocalDate().equals(selectedDate);
    }

    // Método para generar el horario base de 6 a.m. a 9 p.m.
    private List<LocalTime> generateDailySchedule() {
        List<LocalTime> schedule = new ArrayList<>();
        LocalTime startTime = LocalTime.of(6, 0);
        LocalTime endTime = LocalTime.of(21, 0);

        while (!startTime.isAfter(endTime)) {
            schedule.add(startTime);
            startTime = startTime.plusHours(1);
        }
        return schedule;
    }


    // Método para excluir las horas reservadas del horario base
    private List<LocalTime> excludeReservedHours(List<LocalTime> fullSchedule, List<Reservation> reservations) {
        List<LocalTime> availableHours = new ArrayList<>(fullSchedule);

        for (Reservation reservation : reservations) {
            LocalTime start = reservation.getStartsAt().toLocalTime();
            LocalTime end = reservation.getEndsAt().toLocalTime();

            // Remover cada hora dentro del intervalo reservado
            LocalTime time = start;
            while (!time.isAfter(end.minusHours(1))) {
                availableHours.remove(time);
                time = time.plusHours(1);
            }
        }

        return availableHours;
    }

}
