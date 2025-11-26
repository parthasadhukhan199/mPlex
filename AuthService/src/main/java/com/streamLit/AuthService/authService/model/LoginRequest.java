package com.streamLit.AuthService.authService.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
