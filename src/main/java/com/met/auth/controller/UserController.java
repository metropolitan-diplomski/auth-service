package com.met.auth.controller;

import com.met.auth.dto.request.UpdateClientRequest;
import com.met.auth.dto.request.UserCreateRequest;
import com.met.auth.dto.response.UserResponse;
import com.met.auth.exception.AuthServiceException;
import com.met.auth.service.UserService;
import javassist.NotFoundException;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll(@RequestParam(name = "clients") boolean clients) {
        return ResponseEntity.ok(userService.getAllUsers(clients));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestParam(name = "clients") boolean clients, @Valid @RequestBody UserCreateRequest request) throws AuthServiceException {
        return ResponseEntity.ok(userService.createUser(request, clients));
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<UserResponse> updateClient(@PathVariable("id") String id, @Valid @RequestBody UpdateClientRequest request) {
        return ResponseEntity.ok(userService.updateClient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
