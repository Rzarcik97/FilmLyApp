package filmly.service;

import filmly.dto.user.UserPatchRequestDto;
import filmly.dto.user.UserResponseDto;

public interface UserService {

    UserResponseDto getMyProfile(String email);

    UserResponseDto updateMyProfile(String email, UserPatchRequestDto request);

    String changeEmail(String email, String newEmail, String currentPassword);

    String changePassword(String email, String oldPassword, String newPassword);

    UserResponseDto verifyChange(String email, String rawCode);

}
