package com.javabro.springsecurity.service;

import com.javabro.springsecurity.config.JwtService;
import com.javabro.springsecurity.dto.AuthenticationRequest;
import com.javabro.springsecurity.dto.AuthenticationResponse;
import com.javabro.springsecurity.dto.RegisterRequest;
import com.javabro.springsecurity.entities.Role;
import com.javabro.springsecurity.entities.User;
import com.javabro.springsecurity.repository.RoleRepository;
import com.javabro.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    @Autowired
    JwtService jwtService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    public AuthenticationResponse register(@RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(new BCryptPasswordEncoder(10).encode(registerRequest.getPassword()));
        user.setIsActive(true);
        List<Role> roleList = registerRequest.getRoles().stream().map(s -> {
            Role role = roleRepository.findByRoleName(s).orElseThrow();
            if (role != null)
                return role;
            return null;
        }).collect(Collectors.toList());
        user.setRoles(roleList);
        userRepository.save(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtService.generateToken(user));
        return authenticationResponse;
    }

    public AuthenticationResponse getToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(authenticationRequest.getUsername());
        User user = optionalUser.orElseThrow();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (user != null) authenticationResponse.setToken(jwtService.generateToken(user));
        return authenticationResponse;
    }
}