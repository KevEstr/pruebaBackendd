package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.RoomApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomApplicationDAO extends JpaRepository<RoomApplication, Long> {
    void deleteAllByRoomId(Long roomId);
}
