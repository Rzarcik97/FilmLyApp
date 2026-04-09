package filmly.service.impl;

import filmly.dto.user.UserLoginRequestDto;
import filmly.dto.user.UserLoginResponseDto;
import filmly.dto.user.UserRegisterRequestDto;
import filmly.dto.user.UserResponseDto;
import filmly.exception.AuthenticationException;
import filmly.exception.RegistrationException;
import filmly.mapper.UserMapper;
import filmly.model.User;
import filmly.repository.UserRepository;
import filmly.security.JwtUtil;
import filmly.service.AuthenticationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDto register(UserRegisterRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.email()).isPresent()) {
            throw new RegistrationException("Email already exists");
        }
        User user = userMapper.registerModelFromDto(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.password()));
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsernameField());
        user.setCreatedAt(LocalDateTime.now());
        user.setAvatarUrl("default.jpg");
        User savedUser = userRepository.save(user);
        log.info("User register successfully: id = {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            String token = jwtUtil.generateToken(authentication.getName());
            return new UserLoginResponseDto(token);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("invalid login details");
        }
    }
}
