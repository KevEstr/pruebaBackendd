package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.NotificationRequest;
import co.edu.udea.salasinfo.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    NotificationResponse findById(Long id);
    List<NotificationResponse> findAll();
    NotificationResponse save(NotificationRequest notification);
    NotificationResponse deleteById(Long id);
}
