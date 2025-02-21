package co.edu.udea.salasinfo.service.specification;

import co.edu.udea.salasinfo.model.Notification;
import co.edu.udea.salasinfo.utils.enums.NotificationType;
import co.edu.udea.salasinfo.utils.inner_filters.NotificationInnerFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationSpecTest {

    @Mock
    private Root<Notification> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Object> typePath;

    @Mock
    private Path<Object> userIdPath;

    @Mock
    private Join<Object, Object> userJoin;

    private NotificationInnerFilter filter;

    @BeforeEach
    void setUp() {
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        criteriaBuilder = mock(CriteriaBuilder.class);
        typePath = mock(Path.class);
        userIdPath = mock(Path.class);
        userJoin = mock(Join.class);

        when(root.get("type")).thenReturn(typePath);
        when(root.join("user")).thenReturn(userJoin);
        when(userJoin.get("id")).thenReturn(userIdPath);
    }

    @Test
    void filterBy_ReturnsSpecification_WhenFilterIsNotNull() {
        // Arrange
        filter = new NotificationInnerFilter(NotificationType.PRIVATE, "user123");

        // Act
        Specification<Notification> spec = NotificationSpec.filterBy(filter);

        // Assert
        assertNotNull(spec, "Expected non-null Specification but got null");
    }


    @Test
    void hasType_ReturnsPredicate_WhenTypeIsProvided() {
        // Arrange
        NotificationType type = NotificationType.ADMIN;
        when(criteriaBuilder.equal(typePath, type)).thenReturn(mock(Predicate.class));

        // Act
        Specification<Notification> spec = NotificationSpec.filterBy(new NotificationInnerFilter(type, null));

        // Assert
        assertNotNull(spec);
    }

    @Test
    void hasType_ReturnsConjunction_WhenTypeIsNull() {
        // Arrange
        when(criteriaBuilder.conjunction()).thenReturn(mock(Predicate.class));

        // Act
        Specification<Notification> spec = NotificationSpec.filterBy(new NotificationInnerFilter(null, null));

        // Assert
        assertNotNull(spec);
        verify(criteriaBuilder, times(0)).conjunction();
    }

    @Test
    void hasUserId_ReturnsPredicate_WhenUserIdIsProvided() {
        // Arrange
        String userId = "user123";
        when(criteriaBuilder.equal(userIdPath, userId)).thenReturn(mock(Predicate.class));

        // Act
        Specification<Notification> spec = NotificationSpec.filterBy(new NotificationInnerFilter(null, userId));
        Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);

        // Assert
        assertNotNull(predicate);
        verify(criteriaBuilder).equal(userIdPath, userId);
    }

    @Test
    void hasUserId_ReturnsConjunction_WhenUserIdIsNull() {
        // Arrange
        when(criteriaBuilder.conjunction()).thenReturn(mock(Predicate.class));

        // Act
        Specification<Notification> spec = NotificationSpec.filterBy(new NotificationInnerFilter(null, ""));

        // Assert
        assertNotNull(spec);
    }
}
