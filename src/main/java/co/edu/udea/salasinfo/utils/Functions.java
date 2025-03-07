package co.edu.udea.salasinfo.utils;

import co.edu.udea.salasinfo.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Functions {

    // Método auxiliar para verificar si la reserva es en la misma fecha seleccionada
    public static boolean isSameDay(LocalDateTime dateTime, LocalDate selectedDate) {
        return dateTime.toLocalDate().equals(selectedDate);
    }

    // Método para generar el horario base de 6 a.m. a 9 p.m.
    public static List<LocalTime> generateDailySchedule() {
        List<LocalTime> schedule = new ArrayList<>();
        LocalTime startTime = LocalTime.of(6, 0);
        LocalTime endTime = LocalTime.of(22, 0);

        while (!startTime.isAfter(endTime)) {
            schedule.add(startTime);
            startTime = startTime.plusHours(1);
        }
        return schedule;
    }


    // Método para excluir las horas reservadas del horario base
    public static List<LocalTime> excludeReservedHours(List<LocalTime> fullSchedule, List<Reservation> reservations) {
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
