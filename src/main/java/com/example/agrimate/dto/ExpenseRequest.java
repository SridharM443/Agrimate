package com.example.agrimate.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequest {
    private String category;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
}
