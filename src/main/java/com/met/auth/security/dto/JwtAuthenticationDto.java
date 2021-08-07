package com.met.auth.security.dto;

import lombok.Data;

@Data
public class JwtAuthenticationDto {
    private String username;
    private String password;
}
