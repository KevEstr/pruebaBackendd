package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.Application;
import co.edu.udea.salasinfo.model.Implement;
import co.edu.udea.salasinfo.model.Room;

import java.util.List;

public interface ImplementDAO {
    Implement findById(Long id);
    List<Implement> findAll();
    List<Room> findRoomsByImplementId(Long id);
    void deleteById(Long id);
    Implement save(Implement implement);
    List<Implement> findAllById(List<Long> implementIds);
    boolean existsByName(String name);
}
