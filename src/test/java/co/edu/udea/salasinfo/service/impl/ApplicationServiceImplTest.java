package co.edu.udea.salasinfo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import co.edu.udea.salasinfo.dto.request.ApplicationRequest;
import co.edu.udea.salasinfo.dto.response.ApplicationResponse;
import co.edu.udea.salasinfo.dto.response.ImplementResponse;
import co.edu.udea.salasinfo.dto.response.RestrictionResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomApplicationResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomImplementResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.RoomRestrictionResponse;
import co.edu.udea.salasinfo.mapper.request.ApplicationRequestMapper;
import co.edu.udea.salasinfo.mapper.response.ApplicationResponseMapper;
import co.edu.udea.salasinfo.mapper.response.RoomResponseMapper;
import co.edu.udea.salasinfo.model.Application;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.persistence.ApplicationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationDAO applicationDAO;

    @Mock
    private RoomResponseMapper roomResponseMapper;

    @Mock
    private ApplicationRequestMapper applicationRequestMapper;

    @Mock
    private ApplicationResponseMapper applicationResponseMapper;

    @Mock
    private ApplicationResponse mockApplicationResponse;

    @Mock
    private Application mockApplication;

    private List<Application> allApplications;

    private List<Room> roomsForApplication1;
    private List<Room> roomsForApplication2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        Application app1 = new Application(1L, "App1", null);
        Application app2 = new Application(2L, "App2", null);

        allApplications = Arrays.asList(app1, app2);

        Room room1 = Room.builder().id(1L).roomName("Room1").build();
        Room room2 = Room.builder().id(2L).roomName("Room2").build();

        roomsForApplication1 = Arrays.asList(room1, room2);  // Rooms that have App1
        roomsForApplication2 = Collections.singletonList(room1);  // Only Room1 has both App1 and App2
    }

    @Test
    void applicationMatch_success() {
        // Mock findAll to return the mock applications
        when(applicationDAO.findAll()).thenReturn(allApplications);

        // Mock findRoomsByApplicationId for each application
        when(applicationDAO.findRoomsByApplicationId(1L)).thenReturn(roomsForApplication1);
        when(applicationDAO.findRoomsByApplicationId(2L)).thenReturn(roomsForApplication2);

        // Expected matching room (Room1 has both applications)
        RoomResponse roomResponse = new RoomResponse();
        // Create Application Responses
        ApplicationResponse app1Response = new ApplicationResponse(1L, "Microsoft Word");
        ApplicationResponse app2Response = new ApplicationResponse(2L, "Adobe Photoshop");
        List<ApplicationResponse> applicationResponses = Arrays.asList(app1Response, app2Response);

        // Create Room Application Responses
        RoomApplicationResponse roomApp1 = new RoomApplicationResponse(1L, app1Response, "1.0");
        RoomApplicationResponse roomApp2 = new RoomApplicationResponse(2L, app2Response, "2.5");
        List<RoomApplicationResponse> softwareResponses = Arrays.asList(roomApp1, roomApp2);


        ImplementResponse impl1Response = new ImplementResponse(1L, "Proyector");
        ImplementResponse impl2Response = new ImplementResponse(2L, "Pizarrón");

        RoomImplementResponse roomImpl1 = new RoomImplementResponse(1L, impl1Response, "bueno");
        RoomImplementResponse roomImpl2 = new RoomImplementResponse(2L, impl2Response, "regular");
        List<RoomImplementResponse> implementResponses = Arrays.asList(roomImpl1, roomImpl2);

        RestrictionResponse restriction1 = new RestrictionResponse(1L, "No comida ni bebidas");
        RestrictionResponse restriction2 = new RestrictionResponse(2L, "Horario limitado");

        RoomRestrictionResponse roomRestriction1 = new RoomRestrictionResponse(1, restriction1);
        RoomRestrictionResponse roomRestriction2 = new RoomRestrictionResponse(2, restriction2);
        List<RoomRestrictionResponse> restrictionResponses = Arrays.asList(roomRestriction1, roomRestriction2);

        // Crear instancia de RoomResponse

        roomResponse.setId(1);
        roomResponse.setComputerAmount(20);
        roomResponse.setBuilding("Main Building");
        roomResponse.setRoomNum("101");
        roomResponse.setRoomName("Lab");
        roomResponse.setSubRoom(0);
        roomResponse.setSoftware(softwareResponses);
        roomResponse.setRestrictions(restrictionResponses);
        roomResponse.setImplementsList(implementResponses);
        when(roomResponseMapper.toResponses(anyList())).thenReturn(List.of(roomResponse));

        List<String> applicationNames = Arrays.asList("Microsoft Word", "Adobe Photoshop");
        List<RoomResponse> result = applicationService.applicationMatch(applicationNames);

        // Verify the result contains only Room1
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void applicationMatch_noMatchingRoom() {
        // Mock findAll to return the mock applications
        when(applicationDAO.findAll()).thenReturn(allApplications);

        // Mock findRoomsByApplicationId for each application
        when(applicationDAO.findRoomsByApplicationId(1L)).thenReturn(roomsForApplication1);
        when(applicationDAO.findRoomsByApplicationId(2L)).thenReturn(new ArrayList<>()); // No rooms for App2

        List<String> applicationNames = Arrays.asList("App1", "App2");
        List<RoomResponse> result = applicationService.applicationMatch(applicationNames);

        // No rooms should match both applications
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void applicationMatch_applicationNotFound() {
        // Mock findAll to return applications with only App1
        Application app1 = new Application(1L, "App1", null);
        when(applicationDAO.findAll()).thenReturn(List.of(app1));

        // Return rooms for App1 but fail on App2
        when(applicationDAO.findRoomsByApplicationId(1L)).thenReturn(roomsForApplication1);

        List<String> applicationNames = Arrays.asList("App1", "App2");
        List<RoomResponse> result = applicationService.applicationMatch(applicationNames);

        // Verify that no room matches since App2 does not exist
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createApplication_CreatesNewApplication() {
        // Arrange
        ApplicationRequest request = new ApplicationRequest();
        request.setName("Application 1");

        // Simulamos que no existe una aplicación con el mismo nombre
        when(applicationDAO.existsByName(any())).thenReturn(false);

        // Mapeo de la solicitud a la entidad
        when(applicationRequestMapper.toEntity(any())).thenReturn(mockApplication);

        // Guardamos la aplicación
        when(applicationDAO.save(any())).thenReturn(mockApplication);

        // Mapeo de la entidad a la respuesta
        when(applicationResponseMapper.toResponse(any())).thenReturn(mockApplicationResponse);

        // Act
        ApplicationResponse response = applicationService.createApplication(request);

        // Assert
        assertNotNull(response);
        assertEquals(mockApplicationResponse, response);
        verify(applicationDAO).save(any());  // Verificamos que el DAO haya guardado la aplicación
    }

    @Test
    void createApplication_ThrowsExceptionWhenApplicationExists() {
        // Arrange
        ApplicationRequest request = new ApplicationRequest();
        request.setName("Application 1");

        // Simulamos que ya existe una aplicación con el mismo nombre
        when(applicationDAO.existsByName(any())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> applicationService.createApplication(request));
    }

}