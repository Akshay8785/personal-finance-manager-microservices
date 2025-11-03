package com.example.pfm.budgetservice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {
    private final BudgetRepository repo;
    public BudgetController(BudgetRepository repo) { this.repo = repo; }

    @PostMapping
    public ResponseEntity<Budget> create(@RequestBody @Valid Budget budget) {
        return ResponseEntity.ok(repo.save(budget));
    }
    @GetMapping
    public List<Budget> byUser(@RequestParam(required = false) String userId) {
        if (userId == null) return repo.findAll();
        return repo.findByUserId(userId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Budget> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Budget> update(@PathVariable Long id, @RequestBody Budget incoming) {
        return repo.findById(id).map(b -> {
            b.setCategory(incoming.getCategory());
            b.setLimitAmount(incoming.getLimitAmount());
            b.setUserId(incoming.getUserId());
            return ResponseEntity.ok(repo.save(b));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/search")
    public ResponseEntity<Budget> findByUserAndCategory(@RequestParam String userId, @RequestParam String category) {
        return repo.findByUserIdAndCategory(userId, category).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    public record ApplyExpenseRequest(@NotNull @Positive BigDecimal amount, String description) {}
    public record BudgetStatus(boolean exceeded, BigDecimal overBy, BigDecimal spentAmount, BigDecimal limitAmount){}
    @PostMapping("/{id}/apply-expense")
    public ResponseEntity<BudgetStatus> applyExpense(@PathVariable Long id, @RequestBody @Valid ApplyExpenseRequest req) {
        return repo.findById(id).map(b -> {
            b.setSpentAmount(b.getSpentAmount().add(req.amount()));
            repo.save(b);
            BigDecimal over = b.getSpentAmount().subtract(b.getLimitAmount());
            boolean exceeded = over.compareTo(BigDecimal.ZERO) > 0;
            return ResponseEntity.ok(new BudgetStatus(exceeded, exceeded ? over : BigDecimal.ZERO, b.getSpentAmount(), b.getLimitAmount()));
        }).orElse(ResponseEntity.notFound().build());
    }
}
