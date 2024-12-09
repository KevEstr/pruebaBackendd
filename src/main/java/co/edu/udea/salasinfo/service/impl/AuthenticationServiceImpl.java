package co.edu.udea.salasinfo.service.impl;

import co.edu.udea.salasinfo.configuration.security.jwt.JwtService;
import co.edu.udea.salasinfo.dto.request.auth.AuthenticationRequest;
import co.edu.udea.salasinfo.dto.response.auth.AuthenticationResponse;
import co.edu.udea.salasinfo.dto.request.auth.RegisterRequest;
import co.edu.udea.salasinfo.dto.response.auth.RegisterResponse;
import co.edu.udea.salasinfo.exceptions.EmailAlreadyRegisteredException;
import co.edu.udea.salasinfo.exceptions.EntityNotFoundException;
import co.edu.udea.salasinfo.mapper.request.RegisterRequestMapper;
import co.edu.udea.salasinfo.model.User;
import co.edu.udea.salasinfo.persistence.RoleDAO;
import co.edu.udea.salasinfo.repository.UserRepository;
import co.edu.udea.salasinfo.service.AuthenticationService;
import co.edu.udea.salasinfo.utils.Constants;
import co.edu.udea.salasinfo.utils.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RegisterRequestMapper registerRequestMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleDAO roleDAO;

    private static final String ROLE = "role";

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(request.getEmail()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        request.getPassword()
                )
        );

        Map<String, String> claims = new HashMap<>();
        claims.put(ROLE, user.getAuthorities().stream().toList().get(0).getAuthority());
        String token = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(token)
                .role(user.getRole().getRoleName())
                .id(user.getId())
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyRegisteredException(request.getEmail());
        User user = registerRequestMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(roleDAO.findByRoleName(RoleName.Usuario));
        userRepository.save(user);
        return RegisterResponse.builder()
                .message(
                        String.format(Constants.USER_REGISTERED_MESSAGE, user.getFirstname(), user.getLastname(), user.getEmail())
                ).build();
    }
}
