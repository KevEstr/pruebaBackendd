package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.request.ClassReservationRequest;
import co.edu.udea.salasinfo.dto.request.ReservationRequest;
import co.edu.udea.salasinfo.dto.response.reservation.ReservationResponse;
import co.edu.udea.salasinfo.exceptions.RoomOccupiedAtException;
import co.edu.udea.salasinfo.mapper.request.ReservationRequestMapper;
import co.edu.udea.salasinfo.mapper.response.ReservationResponseMapper;
import co.edu.udea.salasinfo.model.Reservation;
import co.edu.udea.salasinfo.model.ReservationState;
import co.edu.udea.salasinfo.persistence.ReservationDAO;
import co.edu.udea.salasinfo.persistence.ReservationStateDAO;
import co.edu.udea.salasinfo.service.ReservationService;
import co.edu.udea.salasinfo.utils.StreamUtils;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import co.edu.udea.salasinfo.utils.enums.ReservationType;
import co.edu.udea.salasinfo.utils.enums.WeekDay;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationDAO reservationDAO;
    private final ReservationStateDAO reservationStateDAO;
    private final ReservationResponseMapper reservationResponseMapper;
    private final ReservationRequestMapper reservationRequestMapper;

    public List<ReservationResponse> findAll() {
        return reservationResponseMapper.toResponses(reservationDAO.findAll());
    }

    //buscar segun su room
    @Override
    public ReservationResponse findById(Long roomId) {
        return reservationResponseMapper.toResponse(reservationDAO.findById(roomId));
    }

    private ReservationResponse save(Reservation reservation) {
        if (reservationDAO.existsByStartsAtAndRoomId(reservation.getStartsAt(), reservation.getRoom()))
            throw new RoomOccupiedAtException(reservation.getRoom().getId().toString(), reservation.getStartsAt());

        reservation.setReservationState(reservationStateDAO.findByState(RStatus.PENDING));
        Reservation result = reservationDAO.save(reservation);
        return reservationResponseMapper.toResponse(result);
    }

    @Transactional
    @Override
    public ReservationResponse saveSingleTimeReservation(ReservationRequest reservation) {
        Reservation entity = reservationRequestMapper.toEntity(reservation);
        entity.setType(ReservationType.ONCE);
        return save(entity);
    }

    @Override
    public ReservationResponse saveFrequentReservation(ReservationRequest reservation) {
        Reservation entity = reservationRequestMapper.toEntity(reservation);
        entity.setType(ReservationType.WEEKLY);
        return save(entity);
    }

    @Transactional
    @Override
    public List<ReservationResponse> saveClass(ClassReservationRequest classReservation) {
        List<Reservation> reservations = generateClassReservations(classReservation);
        reservations.forEach(reservation -> {
            if (reservationDAO.existsByStartsAtAndRoomId(reservation.getStartsAt(), reservation.getRoom()))
                throw new RoomOccupiedAtException(reservation.getRoom().getId().toString(), reservation.getStartsAt());
        });
        return reservationResponseMapper.toResponses(reservationDAO.saveAll(reservations));
    }

    //borrar una reserva de la DB con un id de reserva
    @Override
    public ReservationResponse delete(Long reservationId) {
        Reservation reservation = reservationDAO.findById(reservationId);
        reservationDAO.deleteById(reservationId);
        return reservationResponseMapper.toResponse(reservation);
    }

    //actualizar una reserva existente
    @Transactional
    @Override
    public ReservationResponse update(Long id, ReservationRequest reservation) {
        Reservation entity = reservationDAO.findById(id);
        Reservation mapped = reservationRequestMapper.toEntity(reservation);
        if (mapped.getActivityName() != null) entity.setActivityName(entity.getActivityName());
        if (mapped.getActivityDescription() != null) entity.setActivityDescription(entity.getActivityDescription());
        if (mapped.getStartsAt() != null) entity.setStartsAt(entity.getStartsAt());
        if (mapped.getEndsAt() != null) entity.setEndsAt(entity.getEndsAt());
        if (mapped.getRoom() != null) entity.setRoom(entity.getRoom());
        if (mapped.getReservationState() != null) entity.setReservationState(entity.getReservationState());
        Reservation result = reservationDAO.save(entity);
        return reservationResponseMapper.toResponse(result);

    }

    @Transactional
    @Override
    public ReservationResponse updateState(Long id, RStatus state) {
        Reservation reservation = reservationDAO.findById(id);

        reservation.setReservationState(reservationStateDAO.findByState(state));
        Reservation result = reservationDAO.save(reservation);
        return reservationResponseMapper.toResponse(result);
    }

    /**
     * Filters reservations and returns which match with the given RState
     *
     * @param state status of the reservation
     * @return A list with the filtered reservations
     */
    @Override
    public List<ReservationResponse> findStated(RStatus state) {
        List<Reservation> reservations = reservationDAO.findAll().stream()
                .filter(reservation -> state.equals(reservation.getReservationState().getState())).toList();
        return reservationResponseMapper.toResponses(reservations);
    }


    /**
     * Takes a class reservation and creates reservations between its semester limits
     *
     * @param classReservation Reservation for all semester
     * @return Built reservations
     */
    private List<Reservation> generateClassReservations(ClassReservationRequest classReservation) {
        List<ReservationRequest> reservationRequests = new ArrayList<>();
        classReservation.getSessions().forEach(session -> {

            LocalDateTime startAt = classReservation.getSemesterStartsAt()
                    .atTime(session.getStartsAt())
                    .with(TemporalAdjusters.next(mapWeekDay(session.getDay())));

            while (startAt.isBefore(classReservation.getSemesterEndsAt().atStartOfDay())) {
                ReservationRequest reservationRequest = reservationRequestMapper.toRequest(classReservation);
                reservationRequest.setDate(startAt.toLocalDate());
                reservationRequest.setStartsAt(session.getStartsAt());
                reservationRequest.setEndsAt(session.getEndsAt());
                reservationRequests.add(reservationRequest);
                startAt = startAt.plusWeeks(1L);
            }
        });

        ReservationState reservationState = reservationStateDAO.findByState(RStatus.ACCEPTED);
        return StreamUtils.map(reservationRequestMapper.toEntities(reservationRequests),
                reservation -> {
                    reservation.setReservationState(reservationState);
                    return reservation;
                });
    }

    private DayOfWeek mapWeekDay(WeekDay day) {
        return switch (day) {
            case MONDAY -> DayOfWeek.MONDAY;
            case TUESDAY -> DayOfWeek.TUESDAY;
            case WEDNESDAY -> DayOfWeek.WEDNESDAY;
            case THURSDAY -> DayOfWeek.THURSDAY;
            case FRIDAY -> DayOfWeek.FRIDAY;
            case SATURDAY -> DayOfWeek.SATURDAY;
        };
    }
}



