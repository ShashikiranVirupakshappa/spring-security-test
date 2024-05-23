package com.javabro.springsecurity.controllers;

import com.javabro.springsecurity.dto.AuthenticationRequest;
import com.javabro.springsecurity.dto.AuthenticationResponse;
import com.javabro.springsecurity.dto.RegisterRequest;
import com.javabro.springsecurity.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(authenticationService.register(registerRequest));
    }

    @PostMapping("/")
    public ResponseEntity<AuthenticationResponse> getToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(authenticationService.getToken(authenticationRequest));
    }
}