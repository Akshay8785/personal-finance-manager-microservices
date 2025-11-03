package com.example.pfm.notificationservice;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String category;
    private BigDecimal budgetAmount;
    private String expenseDescription;
    private BigDecimal expenseAmount;
    private Instant createdAt = Instant.now();
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }
    public String getExpenseDescription() { return expenseDescription; }
    public void setExpenseDescription(String expenseDescription) { this.expenseDescription = expenseDescription; }
    public BigDecimal getExpenseAmount() { return expenseAmount; }
    public void setExpenseAmount(BigDecimal expenseAmount) { this.expenseAmount = expenseAmount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
