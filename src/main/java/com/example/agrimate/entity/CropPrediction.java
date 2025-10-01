package com.example.agrimate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CropPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float N;
    private float P;
    private float K;
    private float temperature;
    private float humidity;
    private float ph;
    private float rainfall;

    private String predictedCrop;
    private double probability;

    private LocalDateTime createdAt; // instead of String
}
