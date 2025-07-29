package com.fdmgroup.ExpenseTracker.dto;

import java.math.BigDecimal;
import java.util.Map;

public class ExpenseSummaryDto {
    private BigDecimal totalAmount;
    private int totalCount;
    private Map<String, BigDecimal> categoryBreakdown;

    // Getters and Setters
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Map<String, BigDecimal> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, BigDecimal> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }
} 