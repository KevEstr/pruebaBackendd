package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.Application;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.model.RoomApplication;

import java.util.List;

public interface ApplicationDAO{
    Application findById(Long id);
    List<Application> findAll();
    List<Room> findRoomsByApplicationId(Long id);
    void deleteById(Long id);
    Application save(Application application);
    List<Application> findAllById(List<Long> softwareIds);
    boolean existsByName(String name);
}
