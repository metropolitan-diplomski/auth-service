package com.met.auth.service;

import com.met.auth.dto.request.UserCreateRequest;
import com.met.auth.dto.response.UserResponse;
import com.met.auth.entity.User;
import com.met.auth.exception.AuthServiceException;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse createUser(UserCreateRequest request) throws AuthServiceException;
    UserResponse getProfile();
    UserResponse findUserByEmail(String email) throws AuthServiceException;
    void deleteUser(String id) throws AuthServiceException;
}
