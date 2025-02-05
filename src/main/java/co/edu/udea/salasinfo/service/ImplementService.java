package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.ApplicationRequest;
import co.edu.udea.salasinfo.dto.request.ImplementRequest;
import co.edu.udea.salasinfo.dto.response.ApplicationResponse;
import co.edu.udea.salasinfo.dto.response.ImplementResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;

import java.util.*;

public interface ImplementService {
    // List<RoomResponse> implementMatch(List<String> implementNames);
    List<ImplementResponse> findAll();
    ImplementResponse findById(Long id) ;
    ImplementResponse createImplement(ImplementRequest request) ;
    ImplementResponse updateImplement(Long id, ImplementRequest request);
    ImplementResponse deleteImplement(Long id);
}






