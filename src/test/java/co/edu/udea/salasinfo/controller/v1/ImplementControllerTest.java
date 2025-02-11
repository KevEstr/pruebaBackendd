package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.ImplementRequest;
import co.edu.udea.salasinfo.dto.response.ImplementResponse;
import co.edu.udea.salasinfo.service.ImplementService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImplementController.class)
@AutoConfigureMockMvc(addFilters = false)
class ImplementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImplementService implementService;

    @MockBean
    private JwtService jwtService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(roles = "Admin")
    void findAll_ShouldReturnListOfImplements() throws Exception {
        ImplementResponse response = new ImplementResponse(1L, "Projector");
        when(implementService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/v1/implements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Projector"));
    }

    @Test
    @WithMockUser(roles = "Admin")
    void save_ShouldCreateImplement() throws Exception {
        ImplementRequest request = new ImplementRequest("Whiteboard");
        ImplementResponse response = new ImplementResponse(2L, "Whiteboard");

        when(implementService.createImplement(any(ImplementRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/implements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Whiteboard"));
    }

    @Test
    @WithMockUser(roles = "Admin")
    void findById_ShouldReturnImplement() throws Exception {
        ImplementResponse response = new ImplementResponse(1L, "Projector");

        when(implementService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/v1/implements/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Projector"));
    }

    @Test
    void remove_ShouldDeleteImplement() throws Exception {
        ImplementResponse response = new ImplementResponse(1L, "Projector");

        when(implementService.deleteImplement(1L)).thenReturn(response);

        mockMvc.perform(delete("/v1/implements/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Projector"));
    }

    @Test
    @WithMockUser(roles = "Admin")
    void update_ShouldModifyImplement() throws Exception {
        ImplementRequest request = new ImplementRequest("Smartboard");
        ImplementResponse response = new ImplementResponse(1L, "Smartboard");

        when(implementService.updateImplement(any(Long.class), any(ImplementRequest.class))).thenReturn(response);

        mockMvc.perform(put("/v1/implements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Smartboard"));
    }
}
