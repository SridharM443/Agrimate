package com.example.agrimate.controller;

import com.example.agrimate.model.CropRequest;
import com.example.agrimate.model.CropResponse;
import com.example.agrimate.service.CropService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "*")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping("/predict")
    public CropResponse predictCrop(@RequestBody CropRequest cropRequest) {
        return cropService.predictCrop(cropRequest);
    }
}
