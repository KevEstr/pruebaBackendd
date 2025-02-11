package co.edu.udea.salasinfo.controller.v1;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.ApplicationRequest;
import co.edu.udea.salasinfo.dto.response.ApplicationResponse;
import co.edu.udea.salasinfo.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private ApplicationResponse mockResponse;
    private ApplicationRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockResponse = new ApplicationResponse(1L, "TestApp");
        mockRequest = new ApplicationRequest("TestApp");
    }

    @Test
    @WithMockUser(roles = "Admin")
    void testFindAllApplications() throws Exception {
        when(applicationService.findAll()).thenReturn(List.of(mockResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("TestApp")));

        verify(applicationService, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "Admin")
    void testFindApplicationById() throws Exception {
        when(applicationService.findById(1L)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/applications/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("TestApp")));

        verify(applicationService, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(roles = "Admin")
    void testSaveApplication() throws Exception {
        when(applicationService.createApplication(any(ApplicationRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("TestApp")));

        verify(applicationService, times(1)).createApplication(any(ApplicationRequest.class));
    }

    @Test
    @WithMockUser(roles = "Admin")
    void testDeleteApplication() throws Exception {
        when(applicationService.deleteApplication(1L)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/applications/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("TestApp")));

        verify(applicationService, times(1)).deleteApplication(1L);
    }

    @Test
    @WithMockUser(roles = "Admin")
    void testUpdateApplication() throws Exception {
        when(applicationService.updateApplication(eq(1L), any(ApplicationRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/applications/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("TestApp")));

        verify(applicationService, times(1)).updateApplication(eq(1L), any(ApplicationRequest.class));
    }
}
