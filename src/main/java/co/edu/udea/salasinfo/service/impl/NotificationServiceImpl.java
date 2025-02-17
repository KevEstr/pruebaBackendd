package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.persistence.NotificationDAO;
import co.edu.udea.salasinfo.persistence.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationDAO notificationDAO;
    private final UserDAO userDAO;
}
