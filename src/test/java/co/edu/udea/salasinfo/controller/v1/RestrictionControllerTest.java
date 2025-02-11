package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.RestrictionRequest;
import co.edu.udea.salasinfo.dto.response.RestrictionResponse;
import co.edu.udea.salasinfo.service.RestrictionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestrictionController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestrictionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestrictionService restrictionService;

    @MockBean
    private JwtService jwtService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void findAll_ShouldReturnListOfRestrictions() throws Exception {
        List<RestrictionResponse> restrictions = Arrays.asList(
                new RestrictionResponse(1L, "No food allowed"),
                new RestrictionResponse(2L, "Silence required")
        );

        when(restrictionService.findAllRestrictions()).thenReturn(restrictions);

        mockMvc.perform(get("/v1/restrictions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("No food allowed"));
    }

    @Test
    @WithMockUser(roles = "Admin")
    void save_ShouldCreateRestriction() throws Exception {
        RestrictionRequest request = new RestrictionRequest("No loud music");
        RestrictionResponse response = new RestrictionResponse(3L, "No loud music");

        when(restrictionService.createRestriction(any(RestrictionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/restrictions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.description").value("No loud music"));
    }

    @Test
    void findById_ShouldReturnRestriction() throws Exception {
        RestrictionResponse response = new RestrictionResponse(1L, "No food allowed");

        when(restrictionService.findRestrictionById(1L)).thenReturn(response);

        mockMvc.perform(get("/v1/restrictions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("No food allowed"));
    }

    @Test
    @WithMockUser(roles = "Admin")
    void remove_ShouldDeleteRestriction() throws Exception {
        RestrictionResponse response = new RestrictionResponse(1L, "No food allowed");

        when(restrictionService.deleteRestriction(1L)).thenReturn(response);

        mockMvc.perform(delete("/v1/restrictions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("No food allowed"));
    }

    @Test
    @WithMockUser(roles = "Admin")
    void update_ShouldModifyRestriction() throws Exception {
        RestrictionRequest request = new RestrictionRequest("No cell phones allowed");
        RestrictionResponse response = new RestrictionResponse(1L, "No cell phones allowed");

        when(restrictionService.updateRestriction(any(Long.class), any(RestrictionRequest.class))).thenReturn(response);

        mockMvc.perform(put("/v1/restrictions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("No cell phones allowed"));
    }
}
