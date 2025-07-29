package com.fdmgroup.ExpenseTracker.service;

import com.fdmgroup.ExpenseTracker.dto.CategoryDto;
import com.fdmgroup.ExpenseTracker.exception.ResourceAlreadyExistsException;
import com.fdmgroup.ExpenseTracker.exception.ResourceNotFoundException;
import com.fdmgroup.ExpenseTracker.model.Category;
import com.fdmgroup.ExpenseTracker.model.User;
import com.fdmgroup.ExpenseTracker.repository.CategoryRepository;
import com.fdmgroup.ExpenseTracker.security.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurity userSecurity;

    @Override
    public List<CategoryDto> getAllCategories() {
        String username = userSecurity.getCurrentUsername();
        User user = userService.getUserEntityById(userService.getCurrentUser().getId());
        return categoryRepository.findByUserId(user.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = getCategoryEntityById(id);
        userSecurity.checkUserAccess(category.getUser());
        return convertToDto(category);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        User user = userService.getUserEntityById(userService.getCurrentUser().getId());

        if (categoryRepository.existsByNameAndUserId(categoryDto.getName(), user.getId())) {
            throw new ResourceAlreadyExistsException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setUser(user);

        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = getCategoryEntityById(id);
        userSecurity.checkUserAccess(category.getUser());

        if (!category.getName().equals(categoryDto.getName()) &&
            categoryRepository.existsByNameAndUserId(categoryDto.getName(), category.getUser().getId())) {
            throw new ResourceAlreadyExistsException("Category with this name already exists");
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryEntityById(id);
        userSecurity.checkUserAccess(category.getUser());
        categoryRepository.delete(category);
    }

    private Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
} 