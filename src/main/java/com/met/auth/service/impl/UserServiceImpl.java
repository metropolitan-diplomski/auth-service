package com.met.auth.service.impl;

import com.met.auth.dto.mapper.UserMapper;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) throws AuthServiceException {
        User user = UserMapper.createRequestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        List<String> requestRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();
        for (String role : requestRoles) {
            Role r = roleRepository
                    .findByRoleIgnoreCase(role)
                    .orElseThrow(() -> new AuthServiceException(ErrorCode.NOT_FOUND, "Role not found"));
            roles.add(r);
        }
        user.setRoles(roles);
        user = userRepository.saveAndFlush(user);

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
}
