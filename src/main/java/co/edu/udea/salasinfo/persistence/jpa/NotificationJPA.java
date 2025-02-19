package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Notification;
import co.edu.udea.salasinfo.persistence.NotificationDAO;
import co.edu.udea.salasinfo.repository.NotificationRepository;
import co.edu.udea.salasinfo.service.specification.NotificationSpec;
import co.edu.udea.salasinfo.utils.inner_filters.NotificationInnerFilter;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationJPA implements NotificationDAO {
    private final NotificationRepository notificationRepository;

    @Override
    public Notification findById(Long id) {
        return notificationRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Notification.class.getSimpleName(), id.toString())
                );
    }

    @Override
    public List<Notification> findAll(@Nullable NotificationInnerFilter filter) {
        Specification<Notification> spec = NotificationSpec.filterBy(filter);
        return notificationRepository.findAll(spec);
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification deleteById(Long id) {
        Notification notification = findById(id);
        notificationRepository.delete(notification);
        return notification;
    }
}
