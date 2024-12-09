package co.edu.udea.salasinfo.repository;

import co.edu.udea.salasinfo.model.RoomApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomApplicationRepository extends JpaRepository<RoomApplication, Long> {
}
