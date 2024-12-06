package co.edu.udea.salasinfo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import co.edu.udea.salasinfo.dto.request.RestrictionRequest;
import co.edu.udea.salasinfo.dto.response.RestrictionResponse;
import co.edu.udea.salasinfo.exceptions.EntityAlreadyExistsException;
import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.mapper.request.RestrictionRequestMapper;
import co.edu.udea.salasinfo.mapper.response.RestrictionResponseMapper;
import co.edu.udea.salasinfo.model.Restriction;
import co.edu.udea.salasinfo.persistence.RestrictionDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RestrictionServiceImplTest {

    @Mock
    private RestrictionDAO restrictionDAO;

    @Mock
    private RestrictionResponseMapper restrictionResponseMapper;

    @Mock
    private RestrictionRequestMapper restrictionRequestMapper;

    @InjectMocks
    private RestrictionServiceImpl restrictionService;

    private Restriction mockRestriction;
    private RestrictionResponse mockRestrictionResponse;

    @BeforeEach
    void setUp() {
        // Initialize mock objects
        mockRestriction = new Restriction();
        mockRestriction.setId(1L);
        mockRestriction.setDescription("No food in rooms");

        mockRestrictionResponse = new RestrictionResponse();
        mockRestrictionResponse.setId(1L);
        mockRestrictionResponse.setDescription("No food in rooms");
    }

    @Test
    void findAllRestrictions_ReturnsListOfRestrictions() {
        // Arrange
        when(restrictionDAO.findAll()).thenReturn(Collections.singletonList(mockRestriction));
        when(restrictionResponseMapper.toResponses(any())).thenReturn(Collections.singletonList(mockRestrictionResponse));

        // Act
        List<RestrictionResponse> responses = restrictionService.findAllRestrictions();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(mockRestrictionResponse, responses.get(0));
    }

    @Test
    void createRestriction_CreatesNewRestriction() {
        // Arrange
        RestrictionRequest request = new RestrictionRequest();
        request.setDescription("No food in rooms");

        // Simulamos que no existe una restricción con la misma descripción
        when(restrictionDAO.existsByDescription(any())).thenReturn(false);

        // Mapeo de la solicitud a la entidad
        when(restrictionRequestMapper.toEntity(any())).thenReturn(mockRestriction);

        // Guardamos la restricción
        when(restrictionDAO.save(any())).thenReturn(mockRestriction);

        // Mapeo de la entidad a la respuesta
        when(restrictionResponseMapper.toResponse(any())).thenReturn(mockRestrictionResponse);

        // Act
        RestrictionResponse response = restrictionService.createRestriction(request);

        // Assert
        assertNotNull(response);
        assertEquals(mockRestrictionResponse, response);
        verify(restrictionDAO).save(any());  // Verificamos que el DAO haya guardado la restricción
    }


    @Test
    void createRestriction_ThrowsEntityAlreadyExistsException() {
        // Arrange
        RestrictionRequest request = new RestrictionRequest();
        request.setDescription("No food in rooms");

        // Mock the existsByDescription method to return true
        when(restrictionDAO.existsByDescription(any())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> restrictionService.createRestriction(request));
        verify(restrictionDAO, never()).save(any());
    }

    @Test
    void findRestrictionById_ReturnsRestrictionResponse() {
        // Arrange
        when(restrictionDAO.findById(anyLong())).thenReturn(mockRestriction);
        when(restrictionResponseMapper.toResponse(any())).thenReturn(mockRestrictionResponse);

        // Act
        RestrictionResponse response = restrictionService.findRestrictionById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(mockRestrictionResponse, response);
    }

    @Test
    void deleteRestriction_RemovesRestriction() {
        // Arrange
        when(restrictionDAO.findById(anyLong())).thenReturn(mockRestriction);
        when(restrictionResponseMapper.toResponse(any())).thenReturn(mockRestrictionResponse);
        doNothing().when(restrictionDAO).deleteById(anyLong());

        // Act
        RestrictionResponse response = restrictionService.deleteRestriction(1L);

        // Assert
        assertNotNull(response);
        assertEquals(mockRestrictionResponse, response);
        verify(restrictionDAO).deleteById(1L);
    }

    @Test
    void deleteRestriction_ThrowsEntityNotFoundException() {
        // Arrange
        when(restrictionDAO.findById(anyLong())).thenThrow(new EntityNotFoundException("Not Found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> restrictionService.deleteRestriction(1L));
        verify(restrictionDAO, never()).deleteById(anyLong());
    }
}