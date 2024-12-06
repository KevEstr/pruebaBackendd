package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.ApplicationRequest;
import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.response.ApplicationResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomScheduleResponse;
import co.edu.udea.salasinfo.dto.response.room.SpecificRoomResponse;

import java.time.LocalDateTime;
import java.util.*;

public interface ApplicationService {
    List<RoomResponse> applicationMatch(List<String> applicationNames);
    List<ApplicationResponse> findAll();
    ApplicationResponse findById(Long id) ;
    ApplicationResponse createApplication(ApplicationRequest request) ;
    ApplicationResponse updateApplication(Long id, ApplicationRequest request);
    ApplicationResponse deleteApplication(Long id);
}



