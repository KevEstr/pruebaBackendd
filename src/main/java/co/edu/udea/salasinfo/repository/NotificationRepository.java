package co.edu.udea.salasinfo.repository;

import co.edu.udea.salasinfo.model.Notification;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAll(@Nullable Specification<Notification> specification);
}
