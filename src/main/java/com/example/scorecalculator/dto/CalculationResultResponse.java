package com.example.scorecalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CalculationResultResponse {
    private String companyName;          // 기관명 (예: 국민연금공단)
    private String jobGroup;             // 직무 (예: 전산)
    private String stage;                // 전형 (예: 서류)
    private Double totalScore;           // 최종 인정 가산점 (상한선 적용 후)
    private Double maxScoreCap;          // 최대 가산점 한도
    private Boolean isRegionalTalent;    // 지역인재 우대 해당 여부
    private List<String> appliedCertificates; // 적용된 자격증 이름 목록
}
