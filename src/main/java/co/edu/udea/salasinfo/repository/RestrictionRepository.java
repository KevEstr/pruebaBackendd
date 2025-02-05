package co.edu.udea.salasinfo.repository;

import co.edu.udea.salasinfo.model.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestrictionRepository extends JpaRepository<Restriction, Long> {
    public Optional<Restriction> findByDescription(String description);

    boolean existsByDescription(String description);
}
