package com.met.auth.service;

import com.met.auth.dto.request.UpdateClientRequest;
import com.met.auth.dto.request.UserCreateRequest;
import com.met.auth.dto.response.UserResponse;
import com.met.auth.entity.User;
import com.met.auth.exception.AuthServiceException;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers(boolean clients);
    UserResponse createUser(UserCreateRequest request, boolean clients) throws AuthServiceException;
    UserResponse getById(String id) throws AuthServiceException;
    UserResponse getProfile();
    UserResponse findUserByEmail(String email) throws AuthServiceException;
    void deleteUser(String id) throws AuthServiceException;
    UserResponse updateClient(String id, UpdateClientRequest request);
}
