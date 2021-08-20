package com.met.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRequest {
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
}
