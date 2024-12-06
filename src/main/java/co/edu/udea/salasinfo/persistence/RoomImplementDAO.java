package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.RoomImplement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImplementDAO extends JpaRepository<RoomImplement, Long> {
    void deleteAllByRoomId(Long roomId);
}
