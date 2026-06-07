package filmly.service;

import filmly.dto.user.UserLoginRequestDto;
import filmly.dto.user.UserLoginResponseDto;
import filmly.dto.user.UserRegisterRequestDto;
import filmly.dto.user.UserResponseDto;

public interface AuthenticationService {
    UserResponseDto register(UserRegisterRequestDto userRegistrationRequestDto);

    UserLoginResponseDto login(UserLoginRequestDto request);
}
