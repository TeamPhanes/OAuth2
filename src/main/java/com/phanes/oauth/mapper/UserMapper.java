package com.phanes.oauth.mapper;

import com.phanes.oauth.domain.User;
import com.phanes.oauth.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "profileComment", target = "comment")
    @Mapping(source = "profileImage", target = "image")
    UserDTO UserToUserDTO(User user);
}
