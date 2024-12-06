package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Restriction;
import co.edu.udea.salasinfo.persistence.RestrictionDAO;
import co.edu.udea.salasinfo.repository.RestrictionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestrictionJPATest {

    @InjectMocks
    private RestrictionJPA restrictionJPA;

    @Mock
    private RestrictionRepository restrictionRepository;

    private Restriction restriction;

    @Mock
    private RestrictionDAO restrictionDAO;

    @BeforeEach
    public void setUp() {
        restriction = Restriction.builder()
                .id(1L)
                .description("No food allowed")
                .roomRestrictions(Collections.emptyList()) // Assuming no rooms initially
                .build();
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(restrictionRepository.findById(1L)).thenReturn(Optional.of(restriction));

        // Act
        Restriction foundRestriction = restrictionJPA.findById(1L);

        // Assert
        assertNotNull(foundRestriction);
        assertEquals(restriction.getId(), foundRestriction.getId());
        assertEquals(restriction.getDescription(), foundRestriction.getDescription());
        verify(restrictionRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(restrictionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> restrictionJPA.findById(1L));

        assertEquals("Entity of 'Restriction' type searched with '1' not found", thrown.getMessage());
        verify(restrictionRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        // Arrange
        when(restrictionRepository.findAll()).thenReturn(Collections.singletonList(restriction));

        // Act
        List<Restriction> restrictions = restrictionJPA.findAll();

        // Assert
        assertNotNull(restrictions);
        assertEquals(1, restrictions.size());
        assertEquals(restriction.getId(), restrictions.get(0).getId());
        verify(restrictionRepository).findAll();
    }

    @Test
    void testSave() {
        // Arrange
        when(restrictionRepository.save(restriction)).thenReturn(restriction);

        // Act
        Restriction savedRestriction = restrictionJPA.save(restriction);

        // Assert
        assertNotNull(savedRestriction);
        assertEquals(restriction.getId(), savedRestriction.getId());
        verify(restrictionRepository).save(restriction);
    }

    @Test
    void testFindByDescription_Success() {
        // Arrange
        when(restrictionRepository.findByDescription("No food allowed")).thenReturn(Optional.of(restriction));

        // Act
        Restriction foundRestriction = restrictionJPA.findByDescription("No food allowed");

        // Assert
        assertNotNull(foundRestriction);
        assertEquals(restriction.getId(), foundRestriction.getId());
        assertEquals(restriction.getDescription(), foundRestriction.getDescription());
        verify(restrictionRepository).findByDescription("No food allowed");
    }

    @Test
    void testFindByDescription_NotFound() {
        // Arrange
        when(restrictionRepository.findByDescription("No food allowed")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> restrictionJPA.findByDescription("No food allowed"));

        assertEquals("Restriction with description No food allowed not found", thrown.getMessage());
        verify(restrictionRepository).findByDescription("No food allowed");
    }

    @Test
    void testDeleteById() {
        // Act
        restrictionJPA.deleteById(1L);

        // Assert
        verify(restrictionRepository).deleteById(1L);
    }

    @Test
    void existsByDescription_ShouldReturnTrue_WhenDescriptionExists() {
        // Arrange
        String description = "No food allowed";
        when(restrictionRepository.existsByDescription(description)).thenReturn(true);

        // Act
        boolean result = restrictionRepository.existsByDescription(description);

        // Assert
        assertTrue(result);
        verify(restrictionRepository, times(1)).existsByDescription(description);
    }

    @Test
    void existsByDescription_ShouldReturnFalse_WhenDescriptionDoesNotExist() {
        // Arrange
        String description = "No pets allowed";
        when(restrictionRepository.existsByDescription(description)).thenReturn(false);

        // Act
        boolean result = restrictionRepository.existsByDescription(description);

        // Assert
        assertFalse(result);
        verify(restrictionRepository, times(1)).existsByDescription(description);
    }

}