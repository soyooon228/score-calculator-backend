package com.example.scorecalculator.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CompanyRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String jobGroup; // 예: 전산, 행정, 데이터분석

    @Column(nullable = false)
    private String stage; // 예: DOCUMENT (서류), WRITTEN (필기)

    private Double maxScoreCap; // 최대 인정 가산점 한도 (예: 10.0점)

    @OneToMany(mappedBy = "companyRule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RuleDetail> ruleDetails = new ArrayList<>();
}
