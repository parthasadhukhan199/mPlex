package com.streamLit.AuthService.authService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
