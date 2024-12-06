package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.persistence.RoomDAO;
import co.edu.udea.salasinfo.repository.RoomRepository;
import co.edu.udea.salasinfo.service.specification.RoomSpec;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomJPA implements RoomDAO {
    private final RoomRepository roomRepository;

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Room.class.getSimpleName(), id.toString()));
    }

    @Override
    public List<Room> findAll(@Nullable RoomFilter filter) {
        Specification<Room> specs = RoomSpec.filterBy(filter);
        return roomRepository.findAll(specs);
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return roomRepository.existsById(id);
    }
}
