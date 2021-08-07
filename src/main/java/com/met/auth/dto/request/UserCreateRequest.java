package com.met.auth.dto.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserCreateRequest {

    @NotNull
    @NotBlank(message = "Username cannot be blank!")
    private String username;
    @NotNull
    @NotBlank(message = "Email cannot be blank!")
    private String email;
    @NotNull
    @NotBlank(message = "Name cannot be blank!")
    private String fullName;
    @NotNull
    @NotBlank(message = "Password cannot be blank!")
    private String password;
    @NotNull
    private List<String> roles = new ArrayList<>();
}

