package com.fdmgroup.ExpenseTracker.service;

import com.fdmgroup.ExpenseTracker.dto.ExpenseDto;
import com.fdmgroup.ExpenseTracker.dto.ExpenseSummaryDto;
import com.fdmgroup.ExpenseTracker.exception.ResourceNotFoundException;
import com.fdmgroup.ExpenseTracker.model.Category;
import com.fdmgroup.ExpenseTracker.model.Expense;
import com.fdmgroup.ExpenseTracker.model.User;
import com.fdmgroup.ExpenseTracker.repository.CategoryRepository;
import com.fdmgroup.ExpenseTracker.repository.ExpenseRepository;
import com.fdmgroup.ExpenseTracker.security.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurity userSecurity;

    @Override
    public Page<ExpenseDto> getAllExpenses(Pageable pageable) {
        User user = userService.getUserEntityById(userService.getCurrentUser().getId());
        return expenseRepository.findByUserId(user.getId(), pageable)
                .map(this::convertToDto);
    }

    @Override
    public ExpenseDto getExpenseById(Long id) {
        Expense expense = getExpenseEntityById(id);
        userSecurity.checkUserAccess(expense.getUser());
        return convertToDto(expense);
    }

    @Override
    @Transactional
    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        User user = userService.getUserEntityById(userService.getCurrentUser().getId());
        Category category = categoryRepository.findById(expenseDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        userSecurity.checkUserAccess(category.getUser());

        Expense expense = new Expense();
        expense.setAmount(expenseDto.getAmount());
        expense.setDescription(expenseDto.getDescription());
        expense.setDate(expenseDto.getDate());
        expense.setCategory(category);
        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);
        return convertToDto(savedExpense);
    }

    @Override
    @Transactional
    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {
        Expense expense = getExpenseEntityById(id);
        userSecurity.checkUserAccess(expense.getUser());

        if (expenseDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(expenseDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            userSecurity.checkUserAccess(category.getUser());
            expense.setCategory(category);
        }

        if (expenseDto.getAmount() != null) {
            expense.setAmount(expenseDto.getAmount());
        }
        if (expenseDto.getDescription() != null) {
            expense.setDescription(expenseDto.getDescription());
        }
        if (expenseDto.getDate() != null) {
            expense.setDate(expenseDto.getDate());
        }

        Expense updatedExpense = expenseRepository.save(expense);
        return convertToDto(updatedExpense);
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        Expense expense = getExpenseEntityById(id);
        userSecurity.checkUserAccess(expense.getUser());
        expenseRepository.delete(expense);
    }

    @Override
    public ExpenseSummaryDto getExpenseSummary() {
        User user = userService.getUserEntityById(userService.getCurrentUser().getId());
        BigDecimal totalAmount = expenseRepository.getTotalAmountByUserId(user.getId());
        List<Object[]> categoryBreakdown = expenseRepository.getExpenseSummaryByCategory(user.getId());

        ExpenseSummaryDto summary = new ExpenseSummaryDto();
        summary.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);
        
        Map<String, BigDecimal> breakdownMap = new HashMap<>();
        for (Object[] result : categoryBreakdown) {
            String categoryName = (String) result[0];
            BigDecimal amount = (BigDecimal) result[1];
            breakdownMap.put(categoryName, amount);
        }
        summary.setCategoryBreakdown(breakdownMap);

        return summary;
    }

    @Override
    public ExpenseSummaryDto getExpenseSummaryForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        User user = userService.getUserEntityById(userService.getCurrentUser().getId());
        BigDecimal totalAmount = expenseRepository.getTotalAmountByUserIdAndDateBetween(user.getId(), startDate, endDate);
        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);

        ExpenseSummaryDto summary = new ExpenseSummaryDto();
        summary.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);
        summary.setTotalCount(expenses.size());

        Map<String, BigDecimal> breakdownMap = new HashMap<>();
        expenses.forEach(expense -> {
            String categoryName = expense.getCategory().getName();
            breakdownMap.merge(categoryName, expense.getAmount(), BigDecimal::add);
        });
        summary.setCategoryBreakdown(breakdownMap);

        return summary;
    }

    private Expense getExpenseEntityById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
    }

    private ExpenseDto convertToDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setDate(expense.getDate());
        dto.setCategoryId(expense.getCategory().getId());
        dto.setCategoryName(expense.getCategory().getName());
        return dto;
    }
} 