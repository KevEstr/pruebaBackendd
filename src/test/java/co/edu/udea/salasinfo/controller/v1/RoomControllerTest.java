package co.edu.udea.salasinfo.controller.v1;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.RoomRequest;
import co.edu.udea.salasinfo.dto.request.filter.RoomFilter;
import co.edu.udea.salasinfo.dto.response.room.RoomResponse;
import co.edu.udea.salasinfo.dto.response.room.SpecificRoomResponse;
import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
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
        RoomResponse roomResponse = new RoomResponse(1, 20, "Main Building", "101", "Lab", 0);
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
        RoomRequest roomRequest = new RoomRequest(20, "Main Building", "101", "Lab", 0);
        RoomResponse roomResponse = new RoomResponse(1, 20, "Main Building", "101", "Lab", 0);

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
        RoomResponse roomResponse = new RoomResponse(1, 20, "Main Building", "101", "Lab", 0);

        when(roomService.deleteRoom(1L)).thenReturn(roomResponse);

        mockMvc.perform(delete("/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roomResponse.getId()));
    }

    @Test
    void updateRoom_success() throws Exception {
        RoomRequest roomRequest = new RoomRequest(30, "Main Building", "102", "Lecture Hall", 0);
        RoomResponse roomResponse = new RoomResponse(1, 30, "Main Building", "102", "Lecture Hall", 0);

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
        RoomResponse roomResponse = new RoomResponse(1, 20, "Main Building", "101", "Lab", 0);
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
        RoomRequest invalidRoomRequest = new RoomRequest(0, "", "", "", -1); // Invalid data

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
        RoomRequest invalidRoomRequest = new RoomRequest(-5, "", "Building", "RoomName", 0); // Invalid data

        mockMvc.perform(put("/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRoomRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findFreeRoomAt_success() throws Exception {
        RoomResponse roomResponse = new RoomResponse(1, 20, "Main Building", "101", "Lab", 0);
        List<RoomResponse> roomList = Collections.singletonList(roomResponse);

        when(roomService.findFreeAt(any(LocalDateTime.class))).thenReturn(roomList);

        mockMvc.perform(get("/v1/rooms/free?date=2023-10-10T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(roomResponse.getId()))
                .andExpect(jsonPath("$[0].roomName").value(roomResponse.getRoomName()));
    }
}