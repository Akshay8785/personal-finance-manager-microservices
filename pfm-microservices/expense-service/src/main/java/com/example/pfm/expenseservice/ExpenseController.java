package com.example.pfm.expenseservice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseRepository repo;
    private final RestClient restClient;
    public ExpenseController(ExpenseRepository repo, RestClient restClient) {
        this.repo = repo; this.restClient = restClient;
    }
    public record CreateExpense(@NotBlank String userId, @NotBlank String category, String description,
                                @NotNull @Positive BigDecimal amount) {}
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateExpense req) {
        Expense e = new Expense();
        e.setUserId(req.userId()); e.setCategory(req.category());
        e.setDescription(req.description()); e.setAmount(req.amount());
        repo.save(e);

        var budget = restClient.get()
                .uri("http://localhost:8081/budgets/search?userId={u}&category={c}", req.userId(), req.category())
                .retrieve().toEntity(Map.class);

        if (budget.getStatusCode().is2xxSuccessful() && budget.getBody() != null) {
            Number budgetId = (Number) budget.getBody().get("id");
            Number limitAmount = (Number) budget.getBody().get("limitAmount");

            var status = restClient.post()
                    .uri("http://localhost:8081/budgets/{id}/apply-expense", budgetId.longValue())
                    .body(Map.of("amount", req.amount(), "description", req.description()))
                    .retrieve().toEntity(Map.class);

            if (status.getStatusCode().is2xxSuccessful() && status.getBody() != null) {
                Object exceededRaw = status.getBody().get("exceeded");
                boolean exceeded = false;
                if (exceededRaw instanceof Boolean) exceeded = (Boolean) exceededRaw;
                else if (exceededRaw != null) exceeded = Boolean.parseBoolean(exceededRaw.toString());

                if (exceeded) {
                    restClient.post().uri("http://localhost:8083/notifications")
                            .body(Map.of("userId", req.userId(),
                                         "category", req.category(),
                                         "budgetAmount", limitAmount,
                                         "expenseDescription", req.description(),
                                         "expenseAmount", req.amount()))
                            .retrieve().toBodilessEntity();
                }
            }
        }
        return ResponseEntity.ok(e);
    }
    @GetMapping
    public List<Expense> byUser(@RequestParam(required = false) String userId) {
        if (userId == null) return repo.findAll();
        return repo.findByUserId(userId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Expense> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
