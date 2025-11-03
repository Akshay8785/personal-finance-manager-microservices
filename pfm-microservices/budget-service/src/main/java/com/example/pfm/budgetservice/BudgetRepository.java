package com.example.pfm.budgetservice;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserIdAndCategory(String userId, String category);
    List<Budget> findByUserId(String userId);
}
