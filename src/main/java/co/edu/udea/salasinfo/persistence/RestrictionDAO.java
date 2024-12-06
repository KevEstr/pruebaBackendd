package co.edu.udea.salasinfo.persistence;

import co.edu.udea.salasinfo.model.Application;
import co.edu.udea.salasinfo.model.Restriction;

import java.util.List;


public interface RestrictionDAO{
    Restriction findById(Long id);
    List<Restriction> findAll();
    Restriction save(Restriction restriction);
    Restriction findByDescription(String description);
    void deleteById(Long id);
    List<Restriction> findAllById(List<Long> restrictionIds);
    boolean existsByDescription(String description);
}
