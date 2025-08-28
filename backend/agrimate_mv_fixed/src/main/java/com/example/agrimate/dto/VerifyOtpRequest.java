package com.example.agrimate.dto;

import jakarta.validation.constraints.*;

public record VerifyOtpRequest(
        @Email @NotBlank String email,
        @NotBlank String code
) {}
