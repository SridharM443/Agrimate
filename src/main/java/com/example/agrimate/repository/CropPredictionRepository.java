package com.example.agrimate.repository;

import com.example.agrimate.entity.CropPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropPredictionRepository extends JpaRepository<CropPrediction, Long> {}
