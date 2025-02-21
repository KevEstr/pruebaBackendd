package co.edu.udea.salasinfo.service.specification;

import co.edu.udea.salasinfo.model.Notification;
import co.edu.udea.salasinfo.utils.enums.NotificationType;
import co.edu.udea.salasinfo.utils.inner_filters.NotificationInnerFilter;
import org.springframework.data.jpa.domain.Specification;

public class NotificationSpec {
    private static final String NOTIFICATION_TYPE = "type";
    private static final String USER_ID = "id";
    private static final String USER = "user";

    private NotificationSpec() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Notification> filterBy(NotificationInnerFilter filter) {
        if (filter == null) return null;
        return Specification.where(hasType(filter.getType()))
                .and(hasUserId(filter.getUserId()));
    }

    public static Specification<Notification> hasType(NotificationType type) {
        return (root, query, criteriaBuilder) ->
                type == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(NOTIFICATION_TYPE), type);
    }

    public static Specification<Notification> hasUserId(String userId) {
        return (root, query, criteriaBuilder) ->
                userId == null || userId.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.join(USER).get(USER_ID), userId);
    }


}
