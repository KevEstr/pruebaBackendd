package co.edu.udea.salasinfo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import co.edu.udea.salasinfo.dto.request.user.UserRequest;
import co.edu.udea.salasinfo.dto.response.user.UserResponse;
import co.edu.udea.salasinfo.mapper.request.UserRequestMapper;
import co.edu.udea.salasinfo.mapper.response.UserResponseMapper;
import co.edu.udea.salasinfo.model.Role;
import co.edu.udea.salasinfo.model.User;
import co.edu.udea.salasinfo.persistence.RoleDAO;
import co.edu.udea.salasinfo.persistence.UserDAO;
import co.edu.udea.salasinfo.utils.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private RoleDAO roleDAO;

    @Mock
    private UserResponseMapper userResponseMapper;

    @Mock
    private UserRequestMapper userRequestMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private UserRequest mockUserRequest;
    private UserResponse mockUserResponse;

    @BeforeEach
    public void setUp() {
        // Initialize your mock objects
        mockUser = new User();
        mockUser.setId("1");
        mockUser.setFirstname("John");
        mockUser.setLastname("Doe");
        mockUser.setEmail("john.doe@example.com");

        mockUserRequest = new UserRequest();
        mockUserRequest.setFirstname("Jane");
        mockUserRequest.setLastname("Doe");
        mockUserRequest.setPassword("newPassword");

        mockUserResponse = new UserResponse();
        mockUserResponse.setId("1");
        mockUserResponse.setFirstname("Jane");
        mockUserResponse.setLastname("Doe");
        mockUserResponse.setEmail("john.doe@example.com");
    }

    @Test
    void testFindAll() {
        // Arrange
        when(userDAO.findAll()).thenReturn(Collections.singletonList(mockUser));
        when(userResponseMapper.toResponses(anyList())).thenReturn(Collections.singletonList(mockUserResponse));

        // Act
        List<UserResponse> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jane", result.get(0).getFirstname());
        verify(userDAO).findAll();
        verify(userResponseMapper).toResponses(anyList());
    }

    @Test
    void testUpdate() {
        // Arrange
        when(userDAO.findById("1")).thenReturn(mockUser);
        when(userRequestMapper.toEntity(mockUserRequest)).thenReturn(mockUser);
        when(userDAO.save(mockUser)).thenReturn(mockUser);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        UserResponse result = userService.update("1", mockUserRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstname());
        verify(userDAO).findById("1");
        verify(userRequestMapper).toEntity(mockUserRequest);
        verify(userDAO).save(mockUser);
        verify(userResponseMapper).toResponse(mockUser);
    }

    @Test
    void testUpdateRole() {
        // Arrange
        when(userDAO.findById("1")).thenReturn(mockUser);
        Role mockRole = new Role(); // Assuming Role is a valid entity
        when(roleDAO.findByRoleName(RoleName.Admin)).thenReturn(mockRole);
        when(userDAO.save(mockUser)).thenReturn(mockUser);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        UserResponse result = userService.updateRole("1", RoleName.Admin);

        // Assert
        assertNotNull(result);
        verify(userDAO).findById("1");
        verify(roleDAO).findByRoleName(RoleName.Admin);
        verify(userDAO).save(mockUser);
        verify(userResponseMapper).toResponse(mockUser);
    }

    @Test
    void testFindById() {
        // Arrange
        when(userDAO.findById("1")).thenReturn(mockUser);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        UserResponse result = userService.findById("1");

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstname());
        verify(userDAO).findById("1");
        verify(userResponseMapper).toResponse(mockUser);
    }
}