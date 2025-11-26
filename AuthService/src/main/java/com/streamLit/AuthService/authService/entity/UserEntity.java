package com.streamLit.AuthService.authService.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection = "User")
public class UserEntity {
    @Id
    @Builder.Default
    String userId = UUID.randomUUID().toString();

    @Indexed(unique = true)
    @NotBlank(message = "username is required")
    String userName;

    @Indexed(unique = true)
    @Email
    @NotBlank(message = "Email is required")
    String userEmail;

    @Indexed
    @NotBlank(message = "password is required")
    String password;
}
