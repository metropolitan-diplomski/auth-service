package com.met.auth.service.impl;

import com.met.auth.dto.mapper.UserMapper;
import com.met.auth.dto.request.ChangePasswordRequest;
import com.met.auth.dto.request.UpdateClientRequest;
import com.met.auth.dto.request.UserCreateRequest;
import com.met.auth.dto.response.UserResponse;
import com.met.auth.entity.Role;
import com.met.auth.entity.User;
import com.met.auth.exception.AuthServiceException;
import com.met.auth.exception.ErrorCode;
import com.met.auth.repository.RoleRepository;
import com.met.auth.repository.UserRepository;
import com.met.auth.security.SecurityUtil;
import com.met.auth.service.UserService;
import com.netflix.discovery.converters.Auto;
import liquibase.pro.packaged.A;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityUtil securityUtil;


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(boolean clients) {
        List<UserResponse> allUsers = userRepository.findAll()
                .stream().map(UserMapper::entityToResponse).collect(Collectors.toList());
        List<UserResponse> responses = new ArrayList<>();
        for (UserResponse user : allUsers) {
            if (clients) {
                if (user.getRoles().contains("ROLE_USER")) {
                    responses.add(user);
                }
            } else {
                if (user.getRoles().contains("ROLE_ADMIN")) {
                    responses.add(user);
                }
            }
        }

        return responses;
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request, boolean clients) throws AuthServiceException {
        User user = UserMapper.createRequestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> roles = new HashSet<>();
        if (clients) {
            Role userRole = roleRepository.findByRoleIgnoreCase("ROLE_USER").get();
            roles.add(userRole);
        } else {

            Role userRole = roleRepository.findByRoleIgnoreCase("ROLE_ADMIN").get();
            roles.add(userRole);
        }
        user.setRoles(roles);
        user = userRepository.saveAndFlush(user);

        return UserMapper.entityToResponse(user);
    }

    @Override
    public UserResponse getById(String id) throws AuthServiceException {
        User user = userRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new AuthServiceException(ErrorCode.NOT_FOUND, "User with that id not found."));
        return UserMapper.entityToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getProfile() {
        User user = securityUtil.getLoggedDbUser();
        return UserMapper.entityToResponse(user);

    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserByEmail(String email) throws AuthServiceException {
        User user = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthServiceException(ErrorCode.NOT_FOUND, "User with that id not found."));
        return UserMapper.entityToResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(String id) throws AuthServiceException {
        User user = userRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new AuthServiceException(ErrorCode.NOT_FOUND, "User with that id not found."));

        userRepository.delete(user);
    }

    @Override
    public UserResponse updateClient(String id, UpdateClientRequest request) {
        User user = userRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new AuthServiceException(ErrorCode.NOT_FOUND, "User with that id not found."));
        validateClient(user.getRoles());

        user.setFullName(request.getFullName());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());

        User updated = userRepository.save(user);

        return UserMapper.entityToResponse(updated);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        UUID id = securityUtil.getLoggedDbUser().getId();
        User user = userRepository.findById(id).orElseThrow(
                () -> new AuthServiceException(ErrorCode.NOT_FOUND, "User not found")
        );

        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new AuthServiceException(ErrorCode.PASSWORD_NOT_MATCH, "Current password is not correct");
        }
    }

    void validateClient(Set<Role> roles) {
        boolean isClient = false;
        for (Role role : roles) {
            if (role.getRole().equals("ROLE_USER")) {
                isClient = true;
            }
        }

        if (!isClient) {
            throw new AuthServiceException(ErrorCode.OPERATION_NOT_ALLOWED, "Editing non client users is not allowed");
        }
    }
}
