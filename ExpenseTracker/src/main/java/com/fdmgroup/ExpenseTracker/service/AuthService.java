package com.fdmgroup.ExpenseTracker.service;

import com.fdmgroup.ExpenseTracker.dto.LoginRequest;
import com.fdmgroup.ExpenseTracker.dto.LoginResponse;
import com.fdmgroup.ExpenseTracker.dto.RegisterRequest;
 
public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse registerAdmin(RegisterRequest request);
    LoginResponse login(LoginRequest request);
} 