package com.example.pfm.budgetservice;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "budgets", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "category"}))
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String category;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount = BigDecimal.ZERO;
    public Budget() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getLimitAmount() { return limitAmount; }
    public void setLimitAmount(BigDecimal limitAmount) { this.limitAmount = limitAmount; }
    public BigDecimal getSpentAmount() { return spentAmount; }
    public void setSpentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; }
}
