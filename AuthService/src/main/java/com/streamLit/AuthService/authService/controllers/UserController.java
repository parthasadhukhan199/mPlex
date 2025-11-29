package com.streamLit.AuthService.authService.controllers;


import com.streamLit.AuthService.authService.entity.UserEntity;
import com.streamLit.AuthService.authService.model.LoginRequest;
import com.streamLit.AuthService.authService.model.UserResponse;
import com.streamLit.AuthService.authService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserEntity userEntity) {
        try {
            UserResponse res = userService.createUser(userEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username  or userEmail already exist");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginRequest loginRequest){

        try {

        UserResponse s = userService.logInUser(loginRequest.getUserName(),loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(s);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid username or password");
        }
    }

    @PostMapping("/admin-login")
    public ResponseEntity<?> adminLogIn(@RequestParam String userName, @RequestParam String password){
        UserResponse res = userService.logInAdmin(userName,password);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
