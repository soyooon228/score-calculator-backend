package com.example.scorecalculator.service;

import com.example.scorecalculator.domain.Certificate;
import com.example.scorecalculator.domain.CompanyRule;
import com.example.scorecalculator.domain.RuleDetail;
import com.example.scorecalculator.dto.CalculateRequest;
import com.example.scorecalculator.dto.CalculationResultResponse;
import com.example.scorecalculator.repository.CompanyRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoreCalculatorService {

    private final CompanyRuleRepository companyRuleRepository;

    public List<CalculationResultResponse> calculateScores(CalculateRequest request) {
        List<CompanyRule> allRules = companyRuleRepository.findAllWithDetails();
        List<CalculationResultResponse> results = new ArrayList<>();

        for (CompanyRule rule : allRules) {
            double calculatedSum = 0.0;
            List<String> appliedCerts = new ArrayList<>();

            // 동일 카테고리 중 최고 점수 1개만 인정하기 위한 Map (Category -> Max Score RuleDetail)
            Map<String, RuleDetail> bestRuleByCategory = new HashMap<>();

            for (RuleDetail detail : rule.getRuleDetails()) {
                Certificate cert = detail.getCertificate();
                
                // 사용자가 보유한 자격증인 경우
                if (request.getCertificateIds().contains(cert.getId())) {
                    String category = cert.getCategory();
                    
                    if (!bestRuleByCategory.containsKey(category) || 
                        bestRuleByCategory.get(category).getScore() < detail.getScore()) {
                        bestRuleByCategory.put(category, detail);
                    }
                }
            }

            // 카테고리별 최고 점수 합산
            for (RuleDetail bestDetail : bestRuleByCategory.values()) {
                calculatedSum += bestDetail.getScore();
                appliedCerts.add(bestDetail.getCertificate().getName() + " (+" + bestDetail.getScore() + "점)");
            }

            // 최대 한도(Cap) 적용
            double finalScore = calculatedSum;
            if (rule.getMaxScoreCap() != null) {
                finalScore = Math.min(calculatedSum, rule.getMaxScoreCap());
            }

            // 지역인재 판별
            boolean isRegional = request.getUserRegion() != null && 
                                 request.getUserRegion().equals(rule.getCompany().getRegion());

            results.add(CalculationResultResponse.builder()
                    .companyName(rule.getCompany().getName())
                    .jobGroup(rule.getJobGroup())
                    .stage(rule.getStage())
                    .totalScore(finalScore)
                    .maxScoreCap(rule.getMaxScoreCap())
                    .isRegionalTalent(isRegional)
                    .appliedCertificates(appliedCerts)
                    .build());
        }

        return results;
    }
}
