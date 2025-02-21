package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.ApplicationRequest;
import co.edu.udea.salasinfo.dto.response.ApplicationResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;

import java.util.*;

public interface ApplicationService {
    List<RoomResponse> applicationMatch(List<String> applicationNames);
    List<ApplicationResponse> findAll();
    ApplicationResponse findById(Long id) ;
    ApplicationResponse createApplication(ApplicationRequest request) ;
    ApplicationResponse updateApplication(Long id, ApplicationRequest request);
    ApplicationResponse deleteApplication(Long id);
}



