package co.edu.udea.salasinfo.repository;

import co.edu.udea.salasinfo.model.Implement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImplementRepository extends JpaRepository<Implement, Long> {
    boolean existsByName(String name);
}
