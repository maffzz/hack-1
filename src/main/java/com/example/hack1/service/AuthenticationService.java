package com.example.hack1.service;

import com.example.hack1.domain.User;
import com.example.hack1.dto.JwtAuthenticationResponse;
import com.example.hack1.dto.SigninRequest;
import com.example.hack1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signup(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);
        return response;}

    public JwtAuthenticationResponse signin(SigninRequest request) throws IllegalArgumentException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByEmail(request.getUsername());
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);
        return response;}}