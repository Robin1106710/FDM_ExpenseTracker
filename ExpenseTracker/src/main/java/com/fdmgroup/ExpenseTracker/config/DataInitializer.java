package com.fdmgroup.ExpenseTracker.config;

import com.fdmgroup.ExpenseTracker.model.Category;
import com.fdmgroup.ExpenseTracker.model.Expense;
import com.fdmgroup.ExpenseTracker.model.User;
import com.fdmgroup.ExpenseTracker.model.UserRole;
import com.fdmgroup.ExpenseTracker.repository.CategoryRepository;
import com.fdmgroup.ExpenseTracker.repository.ExpenseRepository;
import com.fdmgroup.ExpenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("!prod")
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            @Autowired UserRepository userRepository,
            @Autowired CategoryRepository categoryRepository,
            @Autowired ExpenseRepository expenseRepository,
            @Autowired PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // Create test users
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setEmail("admin@example.com");
            admin.setRole(UserRole.ROLE_ADMIN);
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("User123!"));
            user.setEmail("user@example.com");
            user.setRole(UserRole.ROLE_USER);
            user.setFirstName("John");
            user.setLastName("Doe");
            userRepository.save(user);

            // Create categories for test user
            List<Category> categories = new ArrayList<>();
            
            Category foodCategory = new Category();
            foodCategory.setName("Food");
            foodCategory.setDescription("Groceries and dining");
            foodCategory.setUser(user);
            categories.add(foodCategory);

            Category transportCategory = new Category();
            transportCategory.setName("Transport");
            transportCategory.setDescription("Public transport and fuel");
            transportCategory.setUser(user);
            categories.add(transportCategory);

            Category entertainmentCategory = new Category();
            entertainmentCategory.setName("Entertainment");
            entertainmentCategory.setDescription("Movies and activities");
            entertainmentCategory.setUser(user);
            categories.add(entertainmentCategory);

            Category billsCategory = new Category();
            billsCategory.setName("Bills");
            billsCategory.setDescription("Monthly bills and utilities");
            billsCategory.setUser(user);
            categories.add(billsCategory);

            categoryRepository.saveAll(categories);

            // Create test expenses
            List<Expense> expenses = new ArrayList<>();

            Expense groceryExpense = new Expense();
            groceryExpense.setAmount(new BigDecimal("50.00"));
            groceryExpense.setDescription("Grocery shopping");
            groceryExpense.setDate(LocalDateTime.now().minusDays(1));
            groceryExpense.setCategory(categories.get(0));
            groceryExpense.setUser(user);
            expenses.add(groceryExpense);

            Expense transportExpense = new Expense();
            transportExpense.setAmount(new BigDecimal("30.00"));
            transportExpense.setDescription("Bus pass");
            transportExpense.setDate(LocalDateTime.now().minusDays(2));
            transportExpense.setCategory(categories.get(1));
            transportExpense.setUser(user);
            expenses.add(transportExpense);

            Expense movieExpense = new Expense();
            movieExpense.setAmount(new BigDecimal("25.00"));
            movieExpense.setDescription("Movie tickets");
            movieExpense.setDate(LocalDateTime.now().minusDays(3));
            movieExpense.setCategory(categories.get(2));
            movieExpense.setUser(user);
            expenses.add(movieExpense);

            Expense billExpense = new Expense();
            billExpense.setAmount(new BigDecimal("100.00"));
            billExpense.setDescription("Electricity bill");
            billExpense.setDate(LocalDateTime.now().minusDays(4));
            billExpense.setCategory(categories.get(3));
            billExpense.setUser(user);
            expenses.add(billExpense);

            expenseRepository.saveAll(expenses);
        };
    }
} 