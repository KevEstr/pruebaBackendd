package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.model.Room;
import jakarta.annotation.Nullable;

import java.util.List;

public interface RoomDAO{
    Room findById(Long id);
    List<Room> findAll(@Nullable RoomFilter filter);
    Room save(Room room);
    void deleteById(Long id);
    boolean existsById(Long id);
}
