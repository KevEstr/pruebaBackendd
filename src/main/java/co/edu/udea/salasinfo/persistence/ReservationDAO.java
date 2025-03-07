package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.Reservation;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.utils.enums.RStatus;
import co.edu.udea.salasinfo.utils.enums.ReservationType;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationDAO{
    Reservation save(Reservation reservation);
    List<Reservation> saveAll(List<Reservation> reservations);
    List<Reservation> findAll();
    List<Reservation> findByType(ReservationType reservationType);
    //boolean existsByStartsAtAndRoomId(LocalDateTime startsAt, Room roomId);
    List<Reservation> findReservationsByRoomIdRoomId(Long roomId);
    boolean existsById(Long reservationId);
    void deleteById(Long reservationId);
    Reservation findById(Long roomId);
    List<Reservation> findByUserId(String userId);
    boolean existsByStartsAtAndRoomAndReservationState_StateNot(LocalDateTime startsAt, Room roomId, RStatus state);

}
