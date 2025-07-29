package com.fdmgroup.ExpenseTracker.service;

import com.fdmgroup.ExpenseTracker.dto.UserDto;
import com.fdmgroup.ExpenseTracker.model.User;

public interface UserService {
    UserDto getCurrentUser();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    User getUserEntityById(Long id);
} 