package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.request.ImplementRequest;
import co.edu.udea.salasinfo.dto.response.ImplementResponse;
import co.edu.udea.salasinfo.mapper.request.ImplementRequestMapper;
import co.edu.udea.salasinfo.mapper.response.ImplementResponseMapper;
import co.edu.udea.salasinfo.model.Implement;
import co.edu.udea.salasinfo.persistence.ImplementDAO;
import co.edu.udea.salasinfo.service.ImplementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ImplementServiceImpl implements ImplementService {
    private final ImplementDAO implementDAO;
    private final ImplementResponseMapper implementResponseMapper;
    private final ImplementRequestMapper implementRequestMapper;

    @Override
    public List<ImplementResponse> findAll() {
        List<Implement> implement = implementDAO.findAll();
        return implementResponseMapper.toResponses(implement);
    }

    @Override
    public ImplementResponse findById(Long id) {
        Implement implement = implementDAO.findById(id);
        return implementResponseMapper.toResponse(implement);
    }

    @Override
    @Transactional
    public ImplementResponse createImplement(ImplementRequest request) {

        if (implementDAO.existsByName(request.getName())) {
            throw new IllegalArgumentException("A implement with name '" + request.getName() + "' already exists.");
        }
        Implement implement = implementRequestMapper.toEntity(request);
        return implementResponseMapper.toResponse(implementDAO.save(implement));
    }

    @Override
    @Transactional
    public ImplementResponse updateImplement(Long id, ImplementRequest request) {
        Implement implement = implementDAO.findById(id);
        if (request.getName() != null) implement.setName(request.getName());
        return implementResponseMapper.toResponse(implementDAO.save(implement));
    }

    @Override
    @Transactional
    public ImplementResponse deleteImplement(Long id) {
        Implement implement = implementDAO.findById(id);
        implementDAO.deleteById(id);
        return implementResponseMapper.toResponse(implement);
    }
}



