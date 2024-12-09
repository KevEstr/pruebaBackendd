package co.edu.udea.salasinfo.persistence.jpa;

import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.model.Role;
import co.edu.udea.salasinfo.repository.RoleRepository;
import co.edu.udea.salasinfo.utils.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleJPATest {

    @InjectMocks
    private RoleJPA roleJPA;

    @Mock
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void setUp() {
        role = Role.builder()
                .id(1L)
                .roleName(RoleName.Admin)
                .build();
    }

    @Test
    void testFindByRoleName_Success() {
        // Arrange
        when(roleRepository.findByRoleName(RoleName.Admin)).thenReturn(Optional.of(role));

        // Act
        Role foundRole = roleJPA.findByRoleName(RoleName.Admin);

        // Assert
        assertNotNull(foundRole);
        assertEquals(role.getId(), foundRole.getId());
        assertEquals(role.getRoleName(), foundRole.getRoleName());
        verify(roleRepository).findByRoleName(RoleName.Admin);
    }

    @Test
    void testFindByRoleName_NotFound() {
        // Arrange
        when(roleRepository.findByRoleName(RoleName.Admin)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> roleJPA.findByRoleName(RoleName.Admin));

        assertEquals("Entity of 'Role' type searched with 'Admin' not found", thrown.getMessage());
        verify(roleRepository).findByRoleName(RoleName.Admin);
    }
}