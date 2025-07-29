package com.fdmgroup.ExpenseTracker.controller;

import com.fdmgroup.ExpenseTracker.dto.ExpenseDto;
import com.fdmgroup.ExpenseTracker.dto.ExpenseSummaryDto;
import com.fdmgroup.ExpenseTracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    @Operation(summary = "Get all expenses for current user (paginated)")
    public ResponseEntity<Page<ExpenseDto>> getAllExpenses(Pageable pageable) {
        return ResponseEntity.ok(expenseService.getAllExpenses(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get expense by ID")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @PostMapping
    @Operation(summary = "Create new expense")
    public ResponseEntity<ExpenseDto> createExpense(@Valid @RequestBody ExpenseDto expenseDto) {
        return ResponseEntity.ok(expenseService.createExpense(expenseDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update expense")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseDto expenseDto) {
        return ResponseEntity.ok(expenseService.updateExpense(id, expenseDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete expense")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get expense summary")
    public ResponseEntity<ExpenseSummaryDto> getExpenseSummary() {
        return ResponseEntity.ok(expenseService.getExpenseSummary());
    }

    @GetMapping("/summary/period")
    @Operation(summary = "Get expense summary for specific period")
    public ResponseEntity<ExpenseSummaryDto> getExpenseSummaryForPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(expenseService.getExpenseSummaryForPeriod(startDate, endDate));
    }
} 