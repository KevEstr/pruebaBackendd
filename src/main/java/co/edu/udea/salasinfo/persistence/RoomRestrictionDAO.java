package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.RoomRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRestrictionDAO extends JpaRepository<RoomRestriction, Long> {

    @Modifying
    void deleteAllByRoomId(@Param("roomId") Long roomId);
}
