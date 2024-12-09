package co.edu.udea.salasinfo.service;

import co.edu.udea.salasinfo.dto.request.RestrictionRequest;
import co.edu.udea.salasinfo.dto.response.RestrictionResponse;

import java.util.List;

public interface RestrictionService {
    List<RestrictionResponse> findAllRestrictions();
    RestrictionResponse createRestriction(RestrictionRequest restriction);
    RestrictionResponse findRestrictionById(Long id) ;
    RestrictionResponse deleteRestriction(Long id);
    RestrictionResponse updateRestriction(Long id, RestrictionRequest request);
}
