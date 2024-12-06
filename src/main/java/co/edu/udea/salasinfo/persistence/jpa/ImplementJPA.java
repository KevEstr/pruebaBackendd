package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.*;
import co.edu.udea.salasinfo.persistence.ImplementDAO;
import co.edu.udea.salasinfo.repository.ImplementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImplementJPA implements ImplementDAO {
    private final ImplementRepository implementRepository;

    @Override
    public Implement findById(Long id) {
        return implementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Implement.class.getSimpleName(), id.toString()));

    }

    @Override
    public List<Implement> findAll() {
        return implementRepository.findAll();
    }

    @Override
    public List<Room> findRoomsByImplementId(Long id) {
        Implement implement = findById(id);
        return implement.getRoomImplements().stream()
                .map(RoomImplement::getRoom)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        implementRepository.deleteById(id);
    }

    @Override
    public Implement save(Implement implement) {
        return implementRepository.save(implement);
    }

    @Override
    public List<Implement> findAllById(List<Long> implementIds) {
        return implementRepository.findAllById(implementIds);
    }

    @Override
    public boolean existsByName(String name) {
        return implementRepository.existsByName(name);
    }


}
