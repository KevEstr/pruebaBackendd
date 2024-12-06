package co.edu.udea.salasinfo.controller.v1;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.dto.response.ApplicationResponse;
import co.edu.udea.salasinfo.dto.response.ImplementResponse;
import co.edu.udea.salasinfo.dto.response.RestrictionResponse;
import co.edu.udea.salasinfo.dto.response.room.*;
import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Application;
import co.edu.udea.salasinfo.model.Implement;
import co.edu.udea.salasinfo.model.Restriction;
import co.edu.udea.salasinfo.model.Room;
import co.edu.udea.salasinfo.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebMvcTest(RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    JwtService jwtService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void findAllRooms_success() throws Exception {

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

        List<RoomResponse> roomList = Collections.singletonList(roomResponse);

        when(roomService.findAll(any(RoomFilter.class))).thenReturn(roomList);

        mockMvc.perform(get("/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(roomResponse.getId()))
                .andExpect(jsonPath("$[0].building").value(roomResponse.getBuilding()));
    }

    @Test
    void saveRoom_success() throws Exception {
        List<Long> implementIds = Arrays.asList(1L, 2L, 3L);
        List<Long> softwareIds = Arrays.asList(10L, 20L);
        List<Long> restrictionsIds = Arrays.asList(10L, 20L);

        RoomRequest roomRequest = new RoomRequest(20, "Main Building", "101", "Lab", 0,null,null, null, null, null);
        SpecificRoomResponse roomResponse = new SpecificRoomResponse();
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
        roomResponse.setImplementList(implementResponses);

        when(roomService.createRoom(any(RoomRequest.class))).thenReturn(roomResponse);

        mockMvc.perform(post("/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(roomResponse.getId()))
                .andExpect(jsonPath("$.building").value(roomResponse.getBuilding()));
    }

    @Test
    void findRoomById_success() throws Exception {
        SpecificRoomResponse specificRoomResponse = new SpecificRoomResponse(1, 20, "Main Building", "101", "Lab", 0, null, null, null);

        when(roomService.findById(1L)).thenReturn(specificRoomResponse);

        mockMvc.perform(get("/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(specificRoomResponse.getId()))
                .andExpect(jsonPath("$.roomName").value(specificRoomResponse.getRoomName()));
    }

    @Test
    void deleteRoom_success() throws Exception {
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

        when(roomService.deleteRoom(1L)).thenReturn(roomResponse);

        mockMvc.perform(delete("/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roomResponse.getId()));
    }

    @Test
    void updateRoom_success() throws Exception {
        List<Long> implementIds = Arrays.asList(1L, 2L, 3L);
        List<Long> softwareIds = Arrays.asList(10L, 20L);
        List<Long> restrictionsIds = Arrays.asList(10L, 20L);

        RoomRequest roomRequest = new RoomRequest(30, "Main Building", "102", "Lecture Hall", 0, null, null, restrictionsIds, null, null);
        SpecificRoomResponse roomResponse = new SpecificRoomResponse();
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
        roomResponse.setImplementList(implementResponses);

        when(roomService.updateRoom(eq(1L), any(RoomRequest.class))).thenReturn(roomResponse);

        mockMvc.perform(put("/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roomResponse.getId()))
                .andExpect(jsonPath("$.roomName").value(roomResponse.getRoomName()));
    }

    @Test
    void findFreeRooms_success() throws Exception {
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
        List<RoomResponse> roomList = Collections.singletonList(roomResponse);

        when(roomService.findFreeAt(any(LocalDateTime.class))).thenReturn(roomList);

        mockMvc.perform(get("/v1/rooms/free")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(roomResponse.getId()));
    }

    @Test
    void findRoomById_notFound() throws Exception {
        when(roomService.findById(1L)).thenThrow(new EntityNotFoundException(Room.class.getSimpleName(), "1"));

        mockMvc.perform(get("/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveRoom_invalidInput() throws Exception {
        List<Long> implementIds = Arrays.asList(-1L,-1L,-1L);
        List<Long> softwareIds = Arrays.asList(-1L, -2L);
        List<Long> restrictionsIds = Arrays.asList(-10L, -20L);
        RoomRequest invalidRoomRequest = new RoomRequest(0, "", "", "", -1, null,null, restrictionsIds, null, null); // Invalid data

        mockMvc.perform(post("/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRoomRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRoom_notFound() throws Exception {
        when(roomService.deleteRoom(1L)).thenThrow(new EntityNotFoundException(Room.class.getSimpleName(), "1"));

        mockMvc.perform(delete("/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void updateRoom_invalidInput() throws Exception {
        List<Long> implementIds = Arrays.asList(-1L,-1L,-1L);
        List<Long> softwareIds = Arrays.asList(-1L, -2L);
        List<Long> restrictionsIds = Arrays.asList(-10L, -20L);

        RoomRequest invalidRoomRequest = new RoomRequest(-5, "", "Building", "RoomName", 0, null,null, restrictionsIds, null, null); // Invalid data

        mockMvc.perform(put("/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRoomRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findFreeRoomAt_success() throws Exception {
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
        List<RoomResponse> roomList = Collections.singletonList(roomResponse);

        when(roomService.findFreeAt(any(LocalDateTime.class))).thenReturn(roomList);

        mockMvc.perform(get("/v1/rooms/free?date=2023-10-10T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(roomResponse.getId()))
                .andExpect(jsonPath("$[0].roomName").value(roomResponse.getRoomName()));
    }
}