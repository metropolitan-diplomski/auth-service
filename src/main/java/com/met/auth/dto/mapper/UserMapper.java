package com.met.auth.dto.mapper;

import com.met.auth.dto.request.UserCreateRequest;
import com.met.auth.dto.response.UserResponse;
import com.met.auth.entity.Role;
import com.met.auth.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse entityToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId().toString());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setCreatedDate(user.getCreatedDate());
        userResponse.setFullName(user.getFullName());
        userResponse.setRoles(user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()));

        return userResponse;
    }

    public static User createRequestToEntity(UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());

        return user;
    }
}
