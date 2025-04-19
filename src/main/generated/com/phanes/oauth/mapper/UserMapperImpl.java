package com.phanes.oauth.mapper;

import com.phanes.oauth.domain.User;
import com.phanes.oauth.dto.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-19T10:59:28+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO UserToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.comment( user.getProfileComment() );
        userDTO.image( user.getProfileImage() );
        userDTO.nickname( user.getNickname() );
        userDTO.gender( user.getGender() );
        userDTO.email( user.getEmail() );
        userDTO.genderMark( user.getGenderMark() );
        userDTO.emailMark( user.getEmailMark() );

        return userDTO.build();
    }
}
