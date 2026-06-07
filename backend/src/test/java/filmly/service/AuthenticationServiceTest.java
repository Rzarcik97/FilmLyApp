package filmly.service;

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
import filmly.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    @DisplayName("""
            register | verify that method register user successfully
             with valid entry
            """)
    void register_success() {
        // given
        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("encodedPass");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("john@example.com");

        UserResponseDto responseDto = new UserResponseDto(
                1L,
                "JohnD",
                "John",
                "Doe",
                "john@example.com",
                null,
                null
        );

        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "JohnD",
                "John",
                "Doe",
                "john@example.com",
                "password",
                "password"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userMapper.registerModelFromDto(request)).thenReturn(user);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPass");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(responseDto);

        // when
        UserResponseDto result = authenticationService.register(request);

        // then
        assertEquals(responseDto, result);
        verify(userRepository).existsByEmail("john@example.com");
        verify(userMapper).registerModelFromDto(request);
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(user);
        verify(userMapper).toDto(savedUser);
    }

    @Test
    @DisplayName("""
            register | verify that method throw RegistrationException
            when email already exists
            """)
    void register_emailExists() {
        // given
        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "JohnD",
                "John",
                "Doe",
                "john@example.com",
                "password",
                "password"
        );
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // when + then
        assertThrows(
                RegistrationException.class,
                () -> authenticationService.register(request)
        );

        verify(userRepository).existsByEmail("john@example.com");
        verifyNoMoreInteractions(userMapper, passwordEncoder, jwtUtil, authenticationManager);
    }

    @Test
    @DisplayName("""
            login | should authenticate user and return token
            """)
    void login_success() {
        // given
        UserLoginRequestDto request = new UserLoginRequestDto(
                "john@example.com", "pass123"
        );

        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtil.generateToken(any())).thenReturn("token123");

        // when
        UserLoginResponseDto result = authenticationService.login(request);

        // then
        assertEquals("token123", result.token());
        verify(authenticationManager).authenticate(any());
        verify(jwtUtil).generateToken(any());
    }

    @Test
    @DisplayName("""
            login | validate that method throw AuthenticationException
            when credentials invalid
            """)
    void login_invalidCredentials() {
        // given
        UserLoginRequestDto request = new UserLoginRequestDto(
                "john@example.com", "wrong"
        );

        doThrow(new BadCredentialsException("bad"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // when + then
        assertThrows(
                AuthenticationException.class,
                () -> authenticationService.login(request)
        );

        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(jwtUtil);
    }
}