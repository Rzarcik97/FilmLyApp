package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.user.UserPatchRequestDto;
import filmly.dto.user.UserRegisterRequestDto;
import filmly.dto.user.UserResponseDto;
import filmly.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "usernameField", target = "username")
    UserResponseDto toDto(User model);

    User registerModelFromDto(UserRegisterRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromPatch(UserPatchRequestDto dto, @MappingTarget User model);
}
