package com.example.pfm.notificationservice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationRepository repo;
    public NotificationController(NotificationRepository repo) { this.repo = repo; }

    public record CreateNotification(@NotBlank String userId, @NotBlank String category,
                                     @NotNull @Positive BigDecimal budgetAmount,
                                     String expenseDescription,
                                     @NotNull @Positive BigDecimal expenseAmount) {}

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody @Valid CreateNotification req) {
        Notification n = new Notification();
        n.setUserId(req.userId());
        n.setCategory(req.category());
        n.setBudgetAmount(req.budgetAmount());
        n.setExpenseDescription(req.expenseDescription());
        n.setExpenseAmount(req.expenseAmount());
        return ResponseEntity.ok(repo.save(n));
    }

    @GetMapping
    public List<Notification> list(@RequestParam(required = false) String userId) {
        if (userId == null) return repo.findAll();
        return repo.findByUserId(userId);
    }
}
