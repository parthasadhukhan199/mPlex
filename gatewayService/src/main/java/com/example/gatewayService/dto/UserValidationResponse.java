package com.example.gatewayService.dto;

import lombok.Data;

@Data
public class UserValidationResponse {
    private String username;
    private String role;
    private long iat;
    private long exp;
}

