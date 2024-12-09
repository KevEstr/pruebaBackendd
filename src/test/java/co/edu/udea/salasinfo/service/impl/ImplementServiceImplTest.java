package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.dto.request.ImplementRequest;
import co.edu.udea.salasinfo.dto.response.ImplementResponse;
import co.edu.udea.salasinfo.mapper.request.ApplicationRequestMapper;
import co.edu.udea.salasinfo.mapper.request.ImplementRequestMapper;
import co.edu.udea.salasinfo.mapper.response.ImplementResponseMapper;
import co.edu.udea.salasinfo.model.Implement;
import co.edu.udea.salasinfo.persistence.ApplicationDAO;
import co.edu.udea.salasinfo.persistence.ImplementDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ImplementServiceImplTest {

    @InjectMocks
    private ImplementServiceImpl implementService;

    @Mock
    private ImplementDAO implementDAO;

    @Mock
    private ImplementRequestMapper implementRequestMapper;

    @Mock
    private ImplementResponseMapper implementResponseMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createImplement_CreatesNewImplement() {
        // Arrange
        ImplementRequest request = new ImplementRequest();
        request.setName("Implement 1");

        Implement implement = new Implement();

        // Simulamos que no existe un implemento con el mismo nombre
        when(implementDAO.existsByName(any())).thenReturn(false);

        // Mapeo de la solicitud a la entidad
        when(implementRequestMapper.toEntity(any(ImplementRequest.class))).thenReturn(implement);

        // Guardamos el implemento
        when(implementDAO.save(any())).thenReturn(implement);

        // Instanciamos manualmente el objeto ImplementResponse
        ImplementResponse response = new ImplementResponse(); // Use actual implementation or constructor
        response.setName("Implement 1");
        // Mapeo de la entidad a la respuesta
        when(implementResponseMapper.toResponse(any())).thenReturn(response);

        // Act
        ImplementResponse result = implementService.createImplement(request);

        // Assert
        assertNotNull(result);
        assertEquals(response, result); // Use the real object for comparison
        verify(implementDAO).save(any());  // Verificamos que el DAO haya guardado el implemento
    }

    @Test
    void createImplement_ThrowsExceptionWhenImplementExists() {
        // Arrange
        ImplementRequest request = new ImplementRequest();
        request.setName("Implement 1");

        // Simulamos que ya existe un implemento con el mismo nombre
        when(implementDAO.existsByName(any())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> implementService.createImplement(request));
    }

}
