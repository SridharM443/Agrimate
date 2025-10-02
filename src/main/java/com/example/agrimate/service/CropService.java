package com.example.agrimate.service;

import com.example.agrimate.entity.CropPrediction;
import com.example.agrimate.model.CropRequest;
import com.example.agrimate.model.CropResponse;
import com.example.agrimate.repository.CropPredictionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CropService {

    private final CropPredictionRepository cropPredictionRepository;
    private final RestTemplate restTemplate;

    public CropService(CropPredictionRepository cropPredictionRepository) {
        this.cropPredictionRepository = cropPredictionRepository;
        this.restTemplate = new RestTemplate();
    }

    public CropResponse predictCrop(CropRequest cropRequest) {
        // 🔹 Build request payload for Python ML API
        Map<String, Object> mlRequest = new HashMap<>();
        mlRequest.put("N", cropRequest.N);
        mlRequest.put("P", cropRequest.P);
        mlRequest.put("K", cropRequest.K);
        mlRequest.put("temperature", cropRequest.temperature);
        mlRequest.put("humidity", cropRequest.humidity);
        mlRequest.put("ph", cropRequest.ph);
        mlRequest.put("rainfall", cropRequest.rainfall);

        // 🔹 Send request to ML API (http://localhost:8000/predict)
        String mlApiUrl = "https://agrimate-gyaz.onrender.com/predict";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mlRequest, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                mlApiUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );

        // 🔹 Extract ML response
        String predictedCrop = "Unknown";
        double probability = 0.0;
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            Map<String, Object> mlResponse = responseEntity.getBody();
            predictedCrop = (String) mlResponse.getOrDefault("recommended_crop", "Unknown");
            probability = ((Number) mlResponse.getOrDefault("probability", 0.0)).doubleValue();
        }

        // 🔹 Save to DB
        CropPrediction prediction = new CropPrediction();
        prediction.setN(cropRequest.N);
        prediction.setP(cropRequest.P);
        prediction.setK(cropRequest.K);
        prediction.setTemperature(cropRequest.temperature);
        prediction.setHumidity(cropRequest.humidity);
        prediction.setPh(cropRequest.ph);
        prediction.setRainfall(cropRequest.rainfall);
        prediction.setPredictedCrop(predictedCrop);
        prediction.setProbability(probability);
        prediction.setCreatedAt(LocalDateTime.now());

        cropPredictionRepository.save(prediction);

        // 🔹 Return DTO
        CropResponse response = new CropResponse();
        response.setRecommendedCrop(predictedCrop);
        response.setProbability(probability);
        return response;
    }
}
