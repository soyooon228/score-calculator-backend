package com.example.scorecalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateRequest {
    private List<Long> certificateIds; // 보유 자격증 ID 리스트 (예: [1, 2, 5])
    private String userRegion;          // 사용자 대학/출신 지역 (예: "전북")
}
