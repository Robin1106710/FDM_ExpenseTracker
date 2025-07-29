package com.fdmgroup.ExpenseTracker.service;

import com.fdmgroup.ExpenseTracker.dto.LoginRequest;
import com.fdmgroup.ExpenseTracker.dto.LoginResponse;
import com.fdmgroup.ExpenseTracker.dto.RegisterRequest;
import com.fdmgroup.ExpenseTracker.exception.ResourceAlreadyExistsException;
import com.fdmgroup.ExpenseTracker.model.User;
import com.fdmgroup.ExpenseTracker.model.UserRole;
import com.fdmgroup.ExpenseTracker.repository.UserRepository;
import com.fdmgroup.ExpenseTracker.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        return createUser(request, UserRole.ROLE_USER);
    }

    @Override
    @Transactional
    public LoginResponse registerAdmin(RegisterRequest request) {
        return createUser(request, UserRole.ROLE_ADMIN);
    }

    private LoginResponse createUser(RegisterRequest request, UserRole role) {
        // Validate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username is already taken");
        }

        // Validate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(role);

        userRepository.save(user);

        // Authenticate
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        String jwt = tokenProvider.generateToken(authentication);
        return new LoginResponse(jwt, user.getUsername());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        String jwt = tokenProvider.generateToken(authentication);
        return new LoginResponse(jwt, request.getUsername());
    }
} 