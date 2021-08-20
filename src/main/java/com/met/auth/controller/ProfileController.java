package com.met.auth.controller;

import com.met.auth.dto.request.ChangePasswordRequest;
import com.met.auth.dto.response.UserResponse;
import com.met.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);

        return ResponseEntity.ok().build();
    }
}
