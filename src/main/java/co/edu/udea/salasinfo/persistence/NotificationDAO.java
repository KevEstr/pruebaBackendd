package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.Notification;
import co.edu.udea.salasinfo.utils.inner_filters.NotificationInnerFilter;
import jakarta.annotation.Nullable;

import java.util.List;

public interface NotificationDAO {
    Notification findById(Long id);
    List<Notification> findAll(@Nullable NotificationInnerFilter filter);
    Notification save(Notification notification);
    Notification deleteById(Long id);
}
