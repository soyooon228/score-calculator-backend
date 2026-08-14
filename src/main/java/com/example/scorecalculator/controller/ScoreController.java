package com.example.scorecalculator.controller;

import com.example.scorecalculator.dto.AlioAnalysisRequest;
import com.example.scorecalculator.dto.AlioAnalysisResponse;
import com.example.scorecalculator.service.AlioParserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // React 연동 허용
public class ScoreController {

    private final AlioParserService alioParserService;

    public ScoreController(AlioParserService alioParserService) {
        this.alioParserService = alioParserService;
    }

    @PostMapping("/analyze-jobs")
    public ResponseEntity<List<AlioAnalysisResponse>> analyzeJobs(@RequestBody AlioAnalysisRequest request) {
        List<AlioAnalysisResponse> response = alioParserService.analyzeJobs(request);
        return ResponseEntity.ok(response);
    }
}
