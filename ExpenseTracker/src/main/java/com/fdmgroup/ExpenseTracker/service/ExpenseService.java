package com.fdmgroup.ExpenseTracker.service;

import com.fdmgroup.ExpenseTracker.dto.ExpenseDto;
import com.fdmgroup.ExpenseTracker.dto.ExpenseSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ExpenseService {
    Page<ExpenseDto> getAllExpenses(Pageable pageable);
    ExpenseDto getExpenseById(Long id);
    ExpenseDto createExpense(ExpenseDto expenseDto);
    ExpenseDto updateExpense(Long id, ExpenseDto expenseDto);
    void deleteExpense(Long id);
    ExpenseSummaryDto getExpenseSummary();
    ExpenseSummaryDto getExpenseSummaryForPeriod(LocalDateTime startDate, LocalDateTime endDate);
} 