package com.example.scorecalculator.config;

import com.example.scorecalculator.domain.Certificate;
import com.example.scorecalculator.domain.Company;
import com.example.scorecalculator.domain.CompanyRule;
import com.example.scorecalculator.domain.RuleDetail;
import com.example.scorecalculator.repository.CertificateRepository;
import com.example.scorecalculator.repository.CompanyRepository;
import com.example.scorecalculator.repository.CompanyRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CertificateRepository certificateRepository;
    private final CompanyRepository companyRepository;
    private final CompanyRuleRepository companyRuleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (certificateRepository.count() > 0) return; // 이미 데이터가 있으면 실행 안 함

        // 1. 자격증 Master 데이터 생성
        Certificate sqld = certificateRepository.save(Certificate.builder().name("SQLD").category("DATA").build());
        Certificate adsp = certificateRepository.save(Certificate.builder().name("ADsP").category("DATA").build());
        Certificate infoTech = certificateRepository.save(Certificate.builder().name("정보처리기사").category("IT").build());
        Certificate history1 = certificateRepository.save(Certificate.builder().name("한국사능력검정 1급").category("HISTORY").build());

        // 2. 기업/기관 정보 생성 (지역인재 테스트용)
        Company nps = companyRepository.save(Company.builder().name("국민연금공단").region("전북").build());
        Company lx = companyRepository.save(Company.builder().name("LX한국국토정보공사").region("전북").build());
        Company kepco = companyRepository.save(Company.builder().name("한국전력공사").region("전남").build());

        // 3. 국민연금공단 전산직 가산점 규칙 생성
        CompanyRule npsRule = CompanyRule.builder()
                .company(nps)
                .jobGroup("전산")
                .stage("서류")
                .maxScoreCap(10.0) // 최대 10점 한도
                .build();

        RuleDetail detail1 = RuleDetail.builder().companyRule(npsRule).certificate(sqld).score(3.0).build();
        RuleDetail detail2 = RuleDetail.builder().companyRule(npsRule).certificate(adsp).score(3.0).build();
        RuleDetail detail3 = RuleDetail.builder().companyRule(npsRule).certificate(infoTech).score(5.0).build();
        RuleDetail detail4 = RuleDetail.builder().companyRule(npsRule).certificate(history1).score(2.0).build();

        npsRule.getRuleDetails().addAll(List.of(detail1, detail2, detail3, detail4));
        companyRuleRepository.save(npsRule);

        // 4. LX한국국토정보공사 데이터직 가산점 규칙 생성
        CompanyRule lxRule = CompanyRule.builder()
                .company(lx)
                .jobGroup("데이터분석")
                .stage("서류")
                .maxScoreCap(8.0)
                .build();

        RuleDetail lxDetail1 = RuleDetail.builder().companyRule(lxRule).certificate(adsp).score(4.0).build();
        RuleDetail lxDetail2 = RuleDetail.builder().companyRule(lxRule).certificate(sqld).score(3.0).build();
        RuleDetail lxDetail3 = RuleDetail.builder().companyRule(lxRule).certificate(history1).score(3.0).build();

        lxRule.getRuleDetails().addAll(List.of(lxDetail1, lxDetail2, lxDetail3));
        companyRuleRepository.save(lxRule);
    }
}
