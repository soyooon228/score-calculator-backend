package com.example.scorecalculator.controller;

import com.example.scorecalculator.dto.CalculateRequest;
import com.example.scorecalculator.dto.CalculationResultResponse;
import com.example.scorecalculator.service.ScoreCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // React 프론트엔드 연동을 위한 CORS 허용
public class ScoreController {

    private final ScoreCalculatorService scoreCalculatorService;

    @PostMapping("/calculate")
    public List<CalculationResultResponse> calculateScores(@RequestBody CalculateRequest request) {
        return scoreCalculatorService.calculateScores(request);
    }
}
