package com.sdet.auto.springboot2api.mappers;

import com.sdet.auto.springboot2api.dto.UserMsDto;
import com.sdet.auto.springboot2api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "Spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // User to User DTO
    @Mappings({
            @Mapping(source = "email", target = "emailAddress"),
            @Mapping(source = "username", target = "userName")
    })
    UserMsDto userToUserDto(User user);

    //List<User> to List<UserMsDto>

    List<UserMsDto> userToUserDto(List<User> users);
}
