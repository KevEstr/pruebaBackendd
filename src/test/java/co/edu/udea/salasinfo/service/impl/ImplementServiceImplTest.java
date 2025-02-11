package co.edu.udea.salasinfo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import co.edu.udea.salasinfo.dto.request.ImplementRequest;
import co.edu.udea.salasinfo.dto.response.ImplementResponse;
import co.edu.udea.salasinfo.mapper.request.ImplementRequestMapper;
import co.edu.udea.salasinfo.mapper.response.ImplementResponseMapper;
import co.edu.udea.salasinfo.model.Implement;
import co.edu.udea.salasinfo.persistence.ImplementDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ImplementServiceImplTest {

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String IMPLEMENT_1_NAME = "Projector";
    private static final String IMPLEMENT_2_NAME = "Whiteboard";
    private static final String NEW_IMPLEMENT_NAME = "Speaker";
    private static final String UPDATED_IMPLEMENT_NAME = "Updated Projector";
    private static final String IMPLEMENT_EXISTS_MESSAGE = "A implement with name '" + IMPLEMENT_1_NAME + "' already exists.";

    @InjectMocks
    private ImplementServiceImpl implementService;

    @Mock
    private ImplementDAO implementDAO;

    @Mock
    private ImplementResponseMapper implementResponseMapper;

    @Mock
    private ImplementRequestMapper implementRequestMapper;

    private Implement mockImplement;
    private ImplementResponse mockImplementResponse;

    @BeforeEach
    void setUp() {
        mockImplement = new Implement(ID_1, IMPLEMENT_1_NAME, null);
        mockImplementResponse = new ImplementResponse(ID_1, IMPLEMENT_1_NAME);
    }

    @Test
    void findAll_ReturnsAllImplements() {
        // Arrange
        Implement implement1 = new Implement(ID_1, IMPLEMENT_1_NAME, null);
        Implement implement2 = new Implement(ID_2, IMPLEMENT_2_NAME, null);
        List<Implement> implementsList = Arrays.asList(implement1, implement2);
        ImplementResponse response1 = new ImplementResponse(ID_1, IMPLEMENT_1_NAME);
        ImplementResponse response2 = new ImplementResponse(ID_2, IMPLEMENT_2_NAME);

        when(implementDAO.findAll()).thenReturn(implementsList);
        when(implementResponseMapper.toResponses(implementsList)).thenReturn(Arrays.asList(response1, response2));

        // Act
        List<ImplementResponse> result = implementService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(IMPLEMENT_1_NAME, result.get(0).getName());
    }

    @Test
    void findById_ReturnsImplement() {
        // Arrange
        when(implementDAO.findById(ID_1)).thenReturn(mockImplement);
        when(implementResponseMapper.toResponse(mockImplement)).thenReturn(mockImplementResponse);

        // Act
        ImplementResponse response = implementService.findById(ID_1);

        // Assert
        assertNotNull(response);
        assertEquals(mockImplementResponse, response);
    }

    @Test
    void createImplement_Success() {
        // Arrange
        ImplementRequest request = new ImplementRequest();
        request.setName(NEW_IMPLEMENT_NAME);

        Implement newImplement = new Implement(ID_2, NEW_IMPLEMENT_NAME, null);
        ImplementResponse newResponse = new ImplementResponse(ID_2, NEW_IMPLEMENT_NAME);

        when(implementDAO.existsByName(NEW_IMPLEMENT_NAME)).thenReturn(false);
        when(implementRequestMapper.toEntity(request)).thenReturn(newImplement);
        when(implementDAO.save(newImplement)).thenReturn(newImplement);
        when(implementResponseMapper.toResponse(newImplement)).thenReturn(newResponse);

        // Act
        ImplementResponse response = implementService.createImplement(request);

        // Assert
        assertNotNull(response);
        assertEquals(NEW_IMPLEMENT_NAME, response.getName());
        verify(implementDAO).save(newImplement);
    }

    @Test
    void createImplement_ThrowsExceptionWhenExists() {
        // Arrange
        ImplementRequest request = new ImplementRequest();
        request.setName(IMPLEMENT_1_NAME);

        when(implementDAO.existsByName(IMPLEMENT_1_NAME)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> implementService.createImplement(request));
        assertEquals(IMPLEMENT_EXISTS_MESSAGE, exception.getMessage());
    }

    @Test
    void updateImplement_Success() {
        // Arrange
        ImplementRequest request = new ImplementRequest();
        request.setName(UPDATED_IMPLEMENT_NAME);

        when(implementDAO.findById(ID_1)).thenReturn(mockImplement);
        when(implementDAO.save(mockImplement)).thenReturn(mockImplement);
        when(implementResponseMapper.toResponse(mockImplement)).thenReturn(mockImplementResponse);

        // Act
        ImplementResponse response = implementService.updateImplement(ID_1, request);

        // Assert
        assertNotNull(response);
        verify(implementDAO).save(mockImplement);
    }

    @Test
    void deleteImplement_Success() {
        // Arrange
        when(implementDAO.findById(ID_1)).thenReturn(mockImplement);
        when(implementResponseMapper.toResponse(mockImplement)).thenReturn(mockImplementResponse);

        // Act
        ImplementResponse response = implementService.deleteImplement(ID_1);

        // Assert
        assertNotNull(response);
        verify(implementDAO).deleteById(ID_1);
    }
}
