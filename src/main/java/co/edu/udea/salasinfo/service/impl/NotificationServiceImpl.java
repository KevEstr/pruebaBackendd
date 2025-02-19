package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.NotificationRequest;
import co.edu.udea.salasinfo.dto.response.NotificationResponse;
import co.edu.udea.salasinfo.mapper.request.NotificationRequestMapper;
import co.edu.udea.salasinfo.mapper.response.NotificationResponseMapper;
import co.edu.udea.salasinfo.model.Notification;
import co.edu.udea.salasinfo.model.User;
import co.edu.udea.salasinfo.persistence.NotificationDAO;
import co.edu.udea.salasinfo.persistence.UserDAO;
import co.edu.udea.salasinfo.service.NotificationService;
import co.edu.udea.salasinfo.utils.TokenHolder;
import co.edu.udea.salasinfo.utils.enums.NotificationType;
import co.edu.udea.salasinfo.utils.enums.RoleName;
import co.edu.udea.salasinfo.utils.inner_filters.NotificationInnerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationDAO notificationDAO;
    private final NotificationResponseMapper responseMapper;
    private final NotificationRequestMapper requestMapper;
    private final JwtService jwtService;
    private final UserDAO userDAO;

    @Override
    public NotificationResponse findById(Long id) {
        return responseMapper.toResponse(
                notificationDAO.findById(id)
        );
    }

    @Override
    public List<NotificationResponse> findAll() {
        // Search user from Token. To don't have to use an id from Frontend and have more security
        User user = userDAO.findById(
                jwtService.extractUsername(TokenHolder.getToken())
        );

        // Creates and personalize the inner filter
        NotificationInnerFilter notificationInnerFilter = new NotificationInnerFilter();
        if (user.getRole().getRoleName().equals(RoleName.Admin)) {
            notificationInnerFilter.setType(NotificationType.Admin);
        } else {
            notificationInnerFilter.setUserId(user.getId());
            notificationInnerFilter.setType(NotificationType.Private);
        }

        // Search whether Admin notifications or private notifications
        return responseMapper.toResponses(notificationDAO.findAll(notificationInnerFilter));
    }

    @Override
    public NotificationResponse save(NotificationRequest notificationRequest) {
        Notification notification = requestMapper.toEntity(notificationRequest);
        notification.setUser(user);
        return responseMapper.toResponse(
                notificationDAO.save(notification)
        );
    }

    @Override
    public NotificationResponse deleteById(Long id) {
        return responseMapper.toResponse(
                notificationDAO.deleteById(id)
        );
    }
}
