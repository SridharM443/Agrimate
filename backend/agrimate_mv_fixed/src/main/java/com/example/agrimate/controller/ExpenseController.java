package com.example.agrimate.controller;

import com.example.agrimate.dto.ExpenseRequest;
import com.example.agrimate.dto.ExpenseResponse;
import com.example.agrimate.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/add")
    public ResponseEntity<ExpenseResponse> create(@RequestBody ExpenseRequest request, Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(expenseService.createExpense(username, request));
    }

    @GetMapping("/get")
    public ResponseEntity<List<ExpenseResponse>> list(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(expenseService.getExpenses(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(
            @PathVariable("id") Long id,   // <-- FIXED
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal UserDetails user) {

        String username = user.getUsername();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Controller user: " + (authentication != null ? authentication.getName() : "null"));

        return ResponseEntity.ok(expenseService.updateExpense(id, username, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable("id") Long id,   // <-- FIXED
            @AuthenticationPrincipal UserDetails user) {

        String username = user.getUsername();
        expenseService.deleteExpense(id, username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Controller user: " + (authentication != null ? authentication.getName() : "null"));

        return ResponseEntity.ok("Expense deleted successfully");
    }
}
