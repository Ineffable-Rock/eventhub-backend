package com.college.eventhub.controller;

import com.college.eventhub.dto.AuthenticationResponse;
import com.college.eventhub.dto.LoginRequest;
import com.college.eventhub.dto.RegisterRequest;
import com.college.eventhub.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        AuthenticationResponse response = authService.register(registerRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest){
        AuthenticationResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

}
