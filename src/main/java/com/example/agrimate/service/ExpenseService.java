package com.example.agrimate.service;

import com.example.agrimate.dto.ExpenseRequest;
import com.example.agrimate.dto.ExpenseResponse;
import com.example.agrimate.entity.Expense;
import com.example.agrimate.entity.User;
import com.example.agrimate.repository.ExpenseRepository;
import com.example.agrimate.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public ExpenseResponse createExpense(String username, ExpenseRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = new Expense();
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setDate(request.getDate());
        expense.setUser(user);

        Expense saved = expenseRepository.save(expense);

        return toResponse(saved);
    }

    public List<ExpenseResponse> getExpenses(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenseRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExpenseResponse updateExpense(Long id, String username, ExpenseRequest request) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!existingExpense.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized update attempt");
        }

        existingExpense.setCategory(request.getCategory());
        existingExpense.setAmount(request.getAmount());
        existingExpense.setDescription(request.getDescription());
        existingExpense.setDate(request.getDate());

        Expense updated = expenseRepository.save(existingExpense);

        return toResponse(updated);
    }

    public void deleteExpense(Long expenseId, String username) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized delete attempt");
        }

        expenseRepository.delete(expense);
    }

    private ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getDate()
        );
    }
}
