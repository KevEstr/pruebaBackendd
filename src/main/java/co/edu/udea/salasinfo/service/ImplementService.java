package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.ImplementRequest;
import co.edu.udea.salasinfo.dto.response.ImplementResponse;

import java.util.*;

public interface ImplementService {
    List<ImplementResponse> findAll();
    ImplementResponse findById(Long id) ;
    ImplementResponse createImplement(ImplementRequest request) ;
    ImplementResponse updateImplement(Long id, ImplementRequest request);
    ImplementResponse deleteImplement(Long id);
}






