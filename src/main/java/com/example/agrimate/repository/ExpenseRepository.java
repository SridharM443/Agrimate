// ExpenseRepository.java
package com.example.agrimate.repository;

import com.example.agrimate.entity.Expense;
import com.example.agrimate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
}
