package filmly.service.impl;

import filmly.dto.user.UserPatchRequestDto;
import filmly.dto.user.UserResponseDto;
import filmly.exception.AuthenticationException;
import filmly.exception.EntityNotFoundException;
import filmly.exception.RegistrationException;
import filmly.mapper.UserMapper;
import filmly.model.User;
import filmly.model.UserVerificationToken;
import filmly.repository.UserRepository;
import filmly.repository.VerificationTokenRepository;
import filmly.security.VerificationTokenService;
import filmly.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public UserResponseDto getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email",email));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateMyProfile(String email, UserPatchRequestDto request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RegistrationException("Username already exists");
        }
        log.info("starting editing user {} profile",email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email",email));
        userMapper.updateFromPatch(request,user);
        User updatedUser = userRepository.save(user);
        log.info("User profile edited successfully");
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    @Override
    public String changeEmail(String email, String newEmail, String currentPassword) {
        log.info("user {} requested change of email",email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email",email));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AuthenticationException("Invalid current password");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RegistrationException("Email already exists");
        }
        return verificationTokenService.createToken(user,
                UserVerificationToken.TokenType.EMAIL_CHANGE,
                newEmail);
    }

    @Transactional
    @Override
    public String changePassword(String email, String oldPassword, String newPassword) {
        log.info("user {} requested change of password",email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email",email));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AuthenticationException("Invalid current password");
        }
        newPassword = passwordEncoder.encode(newPassword);
        return verificationTokenService.createToken(user,
                UserVerificationToken.TokenType.PASSWORD_CHANGE,
                newPassword);
    }

    @Override
    public UserResponseDto verifyChange(String email, String rawCode) {
        log.info("user {} entered verification code",email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email",email));

        UserVerificationToken token = verificationTokenRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Email",email));

        if (verificationTokenService.validateCode(user,rawCode,token.getType())) {
            log.info("verification completed successfully");
            if (token.getType().equals(UserVerificationToken.TokenType.EMAIL_CHANGE)) {
                user.setEmail(token.getNewValue());
                log.info("changing User's email");
            } else if (token.getType().equals(UserVerificationToken.TokenType.PASSWORD_CHANGE)) {
                user.setPassword(token.getNewValue());
                log.info("changing User's password");
            }
        } else {
            throw new AuthenticationException("Invalid verification code");
        }
        userRepository.save(user);
        log.info("deleting verification token");
        verificationTokenRepository.delete(token);
        log.info("token deleted successfully");
        log.info("change ended successfully");
        return userMapper.toDto(user);
    }
}
