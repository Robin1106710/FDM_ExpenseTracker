package com.fdmgroup.ExpenseTracker.repository;

import com.fdmgroup.ExpenseTracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByUserId(Long userId, Pageable pageable);
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = ?1")
    BigDecimal getTotalAmountByUserId(Long userId);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = ?1 AND e.date BETWEEN ?2 AND ?3")
    BigDecimal getTotalAmountByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT e.category.name, SUM(e.amount) FROM Expense e WHERE e.user.id = ?1 GROUP BY e.category.name")
    List<Object[]> getExpenseSummaryByCategory(Long userId);
} 