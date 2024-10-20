package co.edu.udea.salasinfo.controller.v1;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.user.UserRoleRequest;
import co.edu.udea.salasinfo.dto.response.user.RoleResponse;
import co.edu.udea.salasinfo.dto.response.user.UserResponse;
import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.User;
import co.edu.udea.salasinfo.service.UserService;
import co.edu.udea.salasinfo.utils.enums.RoleName;
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

import java.util.Collections;
import java.util.List;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    JwtService jwtService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    // Test for getting all users with an admin role
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_success() throws Exception {
        UserResponse userResponse = new UserResponse("1", "John", "Doe", "john.doe@example.com", new RoleResponse(1L, RoleName.USER));
        List<UserResponse> users = Collections.singletonList(userResponse);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userResponse.getId()))
                .andExpect(jsonPath("$[0].firstname").value(userResponse.getFirstname()))
                .andExpect(jsonPath("$[0].email").value(userResponse.getEmail()));
    }

    // Test for getting a user by ID (success)
    @Test
    void getUserById_success() throws Exception {
        UserResponse userResponse = new UserResponse("1", "John", "Doe", "john.doe@example.com", new RoleResponse(1L, RoleName.USER));

        when(userService.findById("1")).thenReturn(userResponse);

        mockMvc.perform(get("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.firstname").value(userResponse.getFirstname()))
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()));
    }

    // Test for getting a user by ID (not found)
    @Test
    void getUserById_notFound() throws Exception {
        when(userService.findById("1")).thenThrow(new EntityNotFoundException(User.class.getSimpleName(), "1"));

        mockMvc.perform(get("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity of 'User' type searched with '1' not found"));
    }

    // Test for updating a user role (success)
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserRole_success() throws Exception {
        UserRoleRequest roleRequest = new UserRoleRequest(RoleName.ADMIN);
        UserResponse updatedUser = new UserResponse("1", "John", "Doe", "john.doe@example.com", new RoleResponse(1L, RoleName.ADMIN));

        when(userService.updateRole("1", roleRequest.getRoleName())).thenReturn(updatedUser);

        mockMvc.perform(put("/v1/users/1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isOk());
    }

    // Test for an updating user role (not found)
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserRole_userNotFound() throws Exception {
        UserRoleRequest roleRequest = new UserRoleRequest(RoleName.ADMIN);

        when(userService.updateRole("1", roleRequest.getRoleName())).thenThrow(new EntityNotFoundException(User.class.getSimpleName(), "1"));

        mockMvc.perform(put("/v1/users/1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isNotFound());
    }

}