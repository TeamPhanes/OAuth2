package com.phanes.oauth.service;

import com.phanes.oauth.dto.UserDTO;
import com.phanes.oauth.mapper.UserMapper;
import com.phanes.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getUser(Long userId) {
        return userMapper.UserToUserDTO(userRepository.findById(userId).orElseThrow());
    }
}
