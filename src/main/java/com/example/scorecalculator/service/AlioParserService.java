package com.example.scorecalculator.service;

import com.example.scorecalculator.dto.AlioAnalysisRequest;
import com.example.scorecalculator.dto.AlioAnalysisResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class AlioParserService {

    private final String API_KEY = "JBZ0DLLXthPItJbf1I%2FG3U8UVO1fwhw5tL6FEneb5ek6Tovl6V9xHsqE%2F8EFdiYDEEeEhtN%2B7e9PZDMvxHXU1w%3D%3D";

    public List<AlioAnalysisResponse> analyzeJobs(AlioAnalysisRequest request) {
        List<AlioAnalysisResponse> resultList = new ArrayList<>();

        // 1. 테스트/샘플 공고 데이터 구축 (API 키 연동 전/후 모두 작동)
        List<Map<String, String>> sampleJobs = getSampleJobs();

        String userRegion = request.getUserRegion() != null ? request.getUserRegion() : "전북";
        List<String> userCerts = request.getUserCertificates() != null ? request.getUserCertificates() : Collections.emptyList();

        // 2. 키워드 정규식 파싱 및 가산점 계산
        for (Map<String, String> job : sampleJobs) {
            String fullContent = job.get("title") + " " + job.get("content");

            List<String> matchedCerts = new ArrayList<>();
            int certScore = 0;

            for (String cert : userCerts) {
                if (Pattern.compile(cert, Pattern.CASE_INSENSITIVE).matcher(fullContent).find()) {
                    matchedCerts.add(cert);
                    certScore += 3; // 기본 가점 3점
                }
            }

            // 지역인재 조건 탐색
            boolean isRegional = Pattern.compile(userRegion + "|이전지역|지역인재", Pattern.CASE_INSENSITIVE).matcher(fullContent).find()
                    || job.get("location").contains(userRegion);
            int regionalScore = isRegional ? 5 : 0;

            int totalScore = Math.min(certScore + regionalScore, 20);

            resultList.add(new AlioAnalysisResponse(
                    job.get("id"),
                    job.get("companyName"),
                    job.get("title"),
                    job.get("location"),
                    job.get("attachmentUrl"),
                    matchedCerts,
                    certScore,
                    isRegional,
                    regionalScore,
                    totalScore
            ));
        }

        return resultList;
    }

    private List<Map<String, String>> getSampleJobs() {
        List<Map<String, String>> jobs = new ArrayList<>();
        
        Map<String, String> job1 = new HashMap<>();
        job1.put("id", "JOB_NPS_01");
        job1.put("companyName", "국민연금공단");
        job1.put("title", "2026년 신입직원(전산/데이터) 채용 공고");
        job1.put("location", "전북 전주시");
        job1.put("content", "우대사항: SQLD, ADsP, 정보처리기사 보유자. 전북지역 대학 졸업자(이전지역인재) 가점 부여.");
        job1.put("attachmentUrl", "https://example.com/nps.pdf");
        jobs.add(job1);

        Map<String, String> job2 = new HashMap<>();
        job2.put("id", "JOB_LX_01");
        job2.put("companyName", "LX 한국국토정보공사");
        job2.put("title", "2026년 상반기 IT 분야 정규직 채용");
        job2.put("location", "전북 전주시");
        job2.put("content", "필수/우대자격증: SQLD, ADsP, 컴퓨터활용능력 1급. 이전지역인재 우대.");
        job2.put("attachmentUrl", "https://example.com/lx.hwpx");
        jobs.add(job2);

        return jobs;
    }
}
