package co.edu.udea.salasinfo.repository;

import co.edu.udea.salasinfo.model.RoomImplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomImplementRepository extends JpaRepository<RoomImplement, Long> {
}
