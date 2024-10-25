package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.ClassReservationRequest;
import co.edu.udea.salasinfo.dto.request.ReservationRequest;
import co.edu.udea.salasinfo.dto.response.reservation.ReservationResponse;
import co.edu.udea.salasinfo.utils.enums.RStatus;

import java.util.*;

public interface ReservationService {
    List<ReservationResponse> findAll();

    ReservationResponse findById(Long id);

    public ReservationResponse saveSingleTimeReservation(ReservationRequest reservation);
    public ReservationResponse saveFrequentReservation(ReservationRequest reservation);

    List<ReservationResponse> saveClass(ClassReservationRequest reservation);

    ReservationResponse delete(Long reservationId);

    ReservationResponse update(Long id, ReservationRequest reservation);

    ReservationResponse updateState(Long id, RStatus state);
    List<ReservationResponse> findStated(RStatus state);

}



