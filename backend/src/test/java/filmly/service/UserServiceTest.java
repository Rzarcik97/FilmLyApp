package filmly.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import java.util.Optional;
import filmly.dto.user.UserPatchRequestDto;
import filmly.dto.user.UserResponseDto;
import filmly.exception.AuthenticationException;
import filmly.exception.EntityNotFoundException;
import filmly.exception.RegistrationException;
import filmly.mapper.UserMapper;
import filmly.mapper.impl.UserMapperImpl;
import filmly.model.User;
import filmly.model.UserVerificationToken;
import filmly.repository.UserRepository;
import filmly.repository.VerificationTokenRepository;
import filmly.security.VerificationTokenService;
import filmly.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @Mock
    private VerificationTokenService verificationTokenService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("""
            changePassword | Verify that Verification Token is created and
             sent to user with valid entry
            """)
    public void changePassword_WithValidOldPassword_TokenCreatedAndEmailSent() {
        // Given
        String email = "john@example.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("ENCODED");

        String encodedPassword = "encodedNew";
        String rawCode = "123456";
        UserVerificationToken.TokenType type = UserVerificationToken.TokenType.PASSWORD_CHANGE;

        String oldPassword = "old123";
        String newPassword = "new456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(verificationTokenService.createToken(user, type, encodedPassword))
                .thenReturn(rawCode);

        // When
        userServiceImpl.changePassword(email, oldPassword, newPassword);

        // Then
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(oldPassword, user.getPassword());
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(verificationTokenService, times(1)).createToken(user, type, encodedPassword);
        verifyNoMoreInteractions(userRepository, passwordEncoder, verificationTokenService);
    }

    @Test
    @DisplayName("""
            changePassword | Verify that method throws exception with invalid old password
            """)
    public void changePassword_WithInvalidOldPassword_ShouldThrow() {
        // Given
        String email = "john@example.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("ENCODED");

        String wrongPassword = "invalidOld";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, user.getPassword())).thenReturn(false);

        // When / Then
        assertThatThrownBy(() ->
                userServiceImpl.changePassword(email, wrongPassword, "anyNew"))
                .isInstanceOf(AuthenticationException.class);

        verify(passwordEncoder, times(1)).matches(wrongPassword, user.getPassword());
        verify(userRepository, times(1)).findByEmail(email);
        verify(verificationTokenService, never()).createToken(any(), any(), any());
        verifyNoMoreInteractions(userRepository, passwordEncoder, verificationTokenService);
    }

    @Test
    @DisplayName("""
            changeEmail | Verify that method throws exception
            when user with given email is not found
            """)
    void changeEmail_UserNotFound_ShouldThrowException() {
        // Given
        String email = "old@mail.com";
        String newEmail = "new@mail.com";
        String currentPassword = "ENCODED";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(
                EntityNotFoundException.class,
                () -> userServiceImpl.changeEmail(email, newEmail, currentPassword)
        );

        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository, passwordEncoder, verificationTokenService);
    }

    @Test
    @DisplayName("""
            changeEmail | Verify that method throws exception with invalid current password
            """)
    void changeEmail_InvalidCurrentPassword_ShouldThrowException() {
        // Given
        String email = "old@mail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("ENCODED");

        String newEmail = "new@mail.com";
        String currentPassword = "wrongPass";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        // When / Then
        assertThrows(
                AuthenticationException.class,
                () -> userServiceImpl.changeEmail(email, newEmail, currentPassword)
        );

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(currentPassword, user.getPassword());
        verifyNoMoreInteractions(userRepository, passwordEncoder, verificationTokenService);
    }

    @Test
    @DisplayName("""
            changeEmail | Verify that method throws exception when new email already exists
            """)
    void changeEmail_NewEmailAlreadyExists_ShouldThrowException() {
        // Given
        String email = "old@mail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("ENCODED");

        String newEmail = "new@mail.com";
        String currentPassword = "ENCODED";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(userRepository.existsByEmail(newEmail)).thenReturn(true);

        // When / Then
        assertThrows(
                RegistrationException.class,
                () -> userServiceImpl.changeEmail(email, newEmail, currentPassword)
        );

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(currentPassword, user.getPassword());
        verify(userRepository, times(1)).existsByEmail(newEmail);
        verifyNoMoreInteractions(userRepository, passwordEncoder, verificationTokenService);
    }

    @Test
    @DisplayName("""
            changeEmail | Verify that Verification Token is created and
             sent to user with valid entry
            """)
    void changeEmail_ValidData_ShouldCreateTokenAndSendVerificationEmail() {
        // Given
        String email = "old@mail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("ENCODED");

        String newEmail = "new@mail.com";
        String currentPassword = "ENCODED";
        String rawCode = "123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(verificationTokenService.createToken(user,
                UserVerificationToken.TokenType.EMAIL_CHANGE,
                newEmail)).thenReturn(rawCode);

        // When
        userServiceImpl.changeEmail(email, newEmail, currentPassword);

        // Then
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(currentPassword, user.getPassword());
        verify(userRepository, times(1)).existsByEmail(newEmail);
        verify(verificationTokenService, times(1))
                .createToken(user, UserVerificationToken.TokenType.EMAIL_CHANGE, newEmail);
        verifyNoMoreInteractions(userRepository, passwordEncoder, verificationTokenService);
    }

    @Test
    @DisplayName("""
            updateMyProfile | Verify that method throws exception
            when username already exists
            """)
    void updateMyProfile_UsernameAlreadyExists_ShouldThrowException() {
        // Given
        String email = "john@example.com";
        UserPatchRequestDto request = new UserPatchRequestDto(
                "taken_username",
                "John",
                "Doe",
                null);

        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        // When / Then
        assertThrows(
                RegistrationException.class,
                () -> userServiceImpl.updateMyProfile(email, request)
        );

        verify(userRepository, times(1)).existsByUsername(request.username());
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("""
            updateMyProfile | Verify that method throws exception
            when user with given email is not found
            """)
    void updateMyProfile_UserNotFound_ShouldThrowException() {
        // Given
        String email = "missing@example.com";
        UserPatchRequestDto request = new UserPatchRequestDto(
                null,
                "John",
                "Doe",
                null);

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(
                EntityNotFoundException.class,
                () -> userServiceImpl.updateMyProfile(email, request)
        );

        verify(userRepository, times(1)).existsByUsername(request.username());
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("""
            updateMyProfile | Verify that user profile is updated
            and saved successfully with valid input
            """)
    void updateMyProfile_ValidData_ShouldUpdateUserAndReturnDto() {
        // Given
        String email = "john@example.com";

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail(email);
        existingUser.setFirstName("Old");
        existingUser.setLastName("Name");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail(email);
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");

        UserPatchRequestDto request = new UserPatchRequestDto(
                null,
                "John",
                "Doe",
                null);

        UserResponseDto expectedDto = new UserResponseDto(
                1L,
                null,
                "John",
                "Doe",
                email,
                null,
                null);

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        // When
        UserResponseDto actual = userServiceImpl.updateMyProfile(email, request);

        // Then
        assertEquals(expectedDto, actual);

        verify(userRepository, times(1)).existsByUsername(request.username());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).updateFromPatch(request, existingUser);
        verify(userRepository, times(1)).save(existingUser);
        verify(userMapper, times(1)).toDto(updatedUser);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("""
                getMyProfile | Verify that method returns UserResponseDto
                when user exists
            """)
    void getMyProfile_ShouldReturnUserResponseDto() {
        // Given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UserResponseDto result = userServiceImpl.getMyProfile(email);

        // Then
        assertNotNull(result);
        assertEquals(user.getId(), result.userId());
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getFirstName(), result.firstName());
        assertEquals(user.getLastName(), result.lastName());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    @DisplayName("""
            getMyProfile | Verify that method throws EntityNotFoundException
            when user does not exist
            """)
    void getMyProfile_ShouldThrowException_WhenUserNotFound() {
        // Given
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(
                EntityNotFoundException.class,
                () -> userServiceImpl.getMyProfile(email)
        );
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("""
            verifyChange | Verify that email is successfully changed
            when valid code is provided
            """)
    void verifyChange_WithValidEmailChangeToken_EmailUpdatedSuccessfully() {
        // Given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        String code = "123456";

        UserVerificationToken token = new UserVerificationToken();
        token.setType(UserVerificationToken.TokenType.EMAIL_CHANGE);
        token.setNewValue("newmail@example.com");
        token.setVerificationCode(code);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationTokenRepository.findByUser(user)).thenReturn(Optional.of(token));
        when(verificationTokenService.validateCode(user, code, token.getType())).thenReturn(true);

        UserResponseDto expected = new UserResponseDto(
                1L,
                null,
                null,
                null,
                "newmail@example.com",
                null,
                null
        );

        // When
        UserResponseDto actual = userServiceImpl.verifyChange(email, code);

        // Then
        assertEquals(expected, actual);
        verify(userRepository).save(user);
        verify(verificationTokenRepository).delete(token);
    }

    @Test
    @DisplayName("""
            verifyChange | Verify that password is successfully changed
            when valid code is provided
            """)
    void verifyChange_WithValidPasswordChangeToken_PasswordUpdatedSuccessfully() {
        // Given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword("oldEncoded");

        UserVerificationToken token = new UserVerificationToken();
        token.setType(UserVerificationToken.TokenType.PASSWORD_CHANGE);
        token.setNewValue("newEncoded");

        String code = "123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationTokenRepository.findByUser(user)).thenReturn(Optional.of(token));
        when(verificationTokenService.validateCode(user, code, token.getType())).thenReturn(true);

        UserResponseDto expected = new UserResponseDto(
                1L,
                null,
                null,
                null,
                "john@example.com",
                null,
                null
        );

        // When
        UserResponseDto actual = userServiceImpl.verifyChange(email, code);

        // Then
        assertEquals(expected, actual);
        assertEquals("newEncoded", user.getPassword());
        verify(userRepository).save(user);
        verify(verificationTokenRepository).delete(token);
    }

    @Test
    @DisplayName("""
            verifyChange | Verify that method throws EntityNotFoundException
            when user is not found
            """)
    void verifyChange_UserNotFound_ThrowsException() {
        // Given
        String email = "notfound@example.com";
        String rawCode = "123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> userServiceImpl.verifyChange(email, rawCode));
    }

    @Test
    @DisplayName("""
            verifyChange | Verify that method throws EntityNotFoundException
            when verification token is not found
            """)
    void verifyChange_TokenNotFound_ThrowsException() {
        // Given
        String email = "john@example.com";
        User user = new User();
        String rawCode = "123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationTokenRepository.findByUser(user)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> userServiceImpl.verifyChange(email, rawCode));
    }

    @Test
    @DisplayName("""
            verifyChange | Verify that method throws AuthenticationException
            when verification code is invalid
            """)
    void verifyChange_InvalidCode_ThrowsAuthenticationException() {
        // Given
        String email = "john@example.com";
        User user = new User();

        UserVerificationToken token = new UserVerificationToken();
        token.setType(UserVerificationToken.TokenType.EMAIL_CHANGE);

        String rawCode = "badCode";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationTokenRepository.findByUser(user)).thenReturn(Optional.of(token));
        when(verificationTokenService.validateCode(user, rawCode, token.getType()))
                .thenReturn(false);

        // When / Then
        assertThrows(AuthenticationException.class,
                () -> userServiceImpl.verifyChange(email, rawCode));
    }
}