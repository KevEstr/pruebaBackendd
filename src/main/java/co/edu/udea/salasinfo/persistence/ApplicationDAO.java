package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.Application;
import co.edu.udea.salasinfo.model.Room;

import java.util.List;

public interface ApplicationDAO{
    Application findById(Long id);
    List<Application> findAll();
    List<Room> findRoomsByApplicationId(Long id);
}
