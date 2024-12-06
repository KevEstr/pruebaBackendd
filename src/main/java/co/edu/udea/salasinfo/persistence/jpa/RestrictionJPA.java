package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Restriction;
import co.edu.udea.salasinfo.persistence.RestrictionDAO;
import co.edu.udea.salasinfo.repository.RestrictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class RestrictionJPA implements RestrictionDAO {
    private final RestrictionRepository restrictionRepository;

    @Override
    public Restriction findById(Long id) {
        return restrictionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Restriction.class.getSimpleName(), id.toString()));
    }

    @Override
    public List<Restriction> findAll() {
        return restrictionRepository.findAll();
    }

    @Override
    public Restriction save(Restriction restriction) {
        return restrictionRepository.save(restriction);
    }

    @Override
    public Restriction findByDescription(String description) {
        return restrictionRepository.findByDescription(description)
                .orElseThrow(() ->
                        new EntityNotFoundException(Restriction.class.getSimpleName() + " with description " + description + " not found"));
    }

    @Override
    public void deleteById(Long id) {
        restrictionRepository.deleteById(id);
    }

    @Override
    public List<Restriction> findAllById(List<Long> restrictionIds) {
        return restrictionRepository.findAllById(restrictionIds);
    }

    @Override
    public boolean existsByDescription(String description) {
        return restrictionRepository.existsByDescription(description);
    }
}
