package com.streamLit.AuthService.authService.service;

import com.streamLit.AuthService.authService.entity.UserEntity;
import com.streamLit.AuthService.authService.model.TokenResponse;
import com.streamLit.AuthService.authService.model.UserResponse;
import com.streamLit.AuthService.authService.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    JwtService jwtService;


//------Admin Credencials-----
    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;



    public UserResponse createUser(UserEntity userEntity){
        userRepo.save(userEntity);
        Map<String, Object> payload = Map.of(
                "username", userEntity.getUserName(),
                "role", "USER"
        );

        String accessToken = jwtService.createToken(payload, JwtService.TokenType.ACCESS);
        String refreshToken = jwtService.createToken(payload, JwtService.TokenType.REFRESH);
        TokenResponse token = new TokenResponse(accessToken, refreshToken);


        return new UserResponse("successfully created user with id: "+ userEntity.getUserId(), token);
    }

    public UserResponse logInUser(String userName, String password) {

        Optional<UserEntity> optionalUser = userRepo.findByUserName(userName);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("user does not exist");
        }
        UserEntity user =optionalUser.get();
        if ( user.getPassword().equals(password)) {

            Map<String, Object> payload = Map.of(
                    "username", user.getUserName(),
                    "role", "USER"
            );
            String accessToken = jwtService.createToken(payload, JwtService.TokenType.ACCESS);
            String refreshToken = jwtService.createToken(payload, JwtService.TokenType.REFRESH);

            TokenResponse token = new TokenResponse(accessToken, refreshToken);

            return new UserResponse(user.getUserId(), token);
        }else {
        throw new RuntimeException("invalid username or password");

        }


    }

    public UserResponse logInAdmin(String userName, String password){
            if(userName.equals(adminUsername) && password.equals(adminPassword)){
                Map<String, Object> payload = Map.of(
                        "username", "Partha sarathi",
                        "role", "Admin"
                );
                String accessToken = jwtService.createToken(payload, JwtService.TokenType.ACCESS);
                String refreshToken = jwtService.createToken(payload, JwtService.TokenType.REFRESH);

                TokenResponse token = new TokenResponse(accessToken, refreshToken);

                return new UserResponse("Hello Partha!!",token);
            }
            else {
                throw new RuntimeException("Admin credentials are wrong !!");
            }

    }





}
