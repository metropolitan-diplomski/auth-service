package com.met.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateClientRequest {
    @NotNull
    @NotBlank(message = "Email cannot be blank!")
    private String email;
    @NotNull
    @NotBlank(message = "Name cannot be blank!")
    private String fullName;
    @NotNull
    @NotBlank(message = "Name cannot be blank!")
    private String address;
}
