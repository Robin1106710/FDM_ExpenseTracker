package com.fdmgroup.ExpenseTracker.controller;

import com.fdmgroup.ExpenseTracker.dto.LoginRequest;
import com.fdmgroup.ExpenseTracker.dto.LoginResponse;
import com.fdmgroup.ExpenseTracker.dto.RegisterRequest;
import com.fdmgroup.ExpenseTracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/register/admin")
    @Operation(summary = "Register new admin (Requires ADMIN role)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoginResponse> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerAdmin(registerRequest));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
} 