package com.fdmgroup.ExpenseTracker.service;

import com.fdmgroup.ExpenseTracker.dto.UserDto;
import com.fdmgroup.ExpenseTracker.exception.ResourceNotFoundException;
import com.fdmgroup.ExpenseTracker.model.User;
import com.fdmgroup.ExpenseTracker.repository.UserRepository;
import com.fdmgroup.ExpenseTracker.security.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSecurity userSecurity;

    @Override
    public UserDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = getUserEntityById(id);
        userSecurity.checkUserAccess(user);
        return convertToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = getUserEntityById(id);
        userSecurity.checkUserAccess(user);

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserEntityById(id);
        userSecurity.checkUserAccess(user);
        userRepository.delete(user);
    }

    @Override
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        return dto;
    }
} 