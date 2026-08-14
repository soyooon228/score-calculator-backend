package com.example.scorecalculator.service;

import com.example.scorecalculator.dto.AlioAnalysisRequest;
import com.example.scorecalculator.dto.AlioAnalysisResponse;
import com.example.scorecalculator.dto.CompanyDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class AlioParserService {

    // 첨부해주신 API End Point 및 인증키
    private final String BASE_URL = "https://apis.data.go.kr/1051000/public_inst";
    private final String SERVICE_KEY = "JBZ0DLLXthPItJbf1I%2FG3U8UVO1fwhw5tL6FEneb5ek6Tovl6V9xHsqE%2F8EFdiYDEEeEhtN%2B7e9PZDMvxHXU1w%3D%3D";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 🌐 공공데이터포털 API를 호출하여 전체 공공기관 목록 가져오기
     */
    public List<CompanyDto> getAllCompanies() {
        List<CompanyDto> companyList = new ArrayList<>();
        // 첫 번째 드롭다운 옵션: 전체 보기
        companyList.add(new CompanyDto("all", "🏢 전체 공공기관 채용공고 보기"));

        try {
            // URI가 인코딩된 인증키를 다시 인코딩하지 않도록 build(true) 사용
            URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/getPublicInstList")
                    .queryParam("serviceKey", SERVICE_KEY)
                    .queryParam("pageNo", "1")
                    .queryParam("numOfRows", "1000") // 전체 기관 수용
                    .queryParam("resultType", "json")
                    .build(true)
                    .toUri();

            String response = restTemplate.getForObject(uri, String.class);
            JsonNode root = objectMapper.readTree(response);

            // API 응답 구조 파싱 (공공데이터포털 JSON 표준)
            JsonNode items = root.path("response").path("body").path("items").path("item");

            if (items.isArray()) {
                int index = 1;
                for (JsonNode item : items) {
                    // 기관명 필드 추출 (공공기관 API 명세상 instNm 또는 apstNm 등)
                    String name = item.path("instNm").asText();
                    if (name.isEmpty()) {
                        name = item.path("apstNm").asText();
                    }

                    if (!name.isEmpty()) {
                        companyList.add(new CompanyDto("comp_" + (index++), name));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("공공기관 목록 API 호출 실패: " + e.getMessage());
            // API 오류 발생 시 백업 데이터로 기본 주요 기관 제공
            return getFallbackCompanies();
        }

        return companyList;
    }

    /**
     * API 호출 예외 시 대체용 기본 기관 목록
     */
    private List<CompanyDto> getFallbackCompanies() {
        return Arrays.asList(
                new CompanyDto("all", "🏢 전체 공공기관 채용공고 보기"),
                new CompanyDto("nps", "국민연금공단"),
                new CompanyDto("lx", "LX 한국국토정보공사"),
                new CompanyDto("kepco", "한국전력공사"),
                new CompanyDto("kwater", "한국수자원공사"),
                new CompanyDto("korail", "한국철도공사 (코레일)"),
                new CompanyDto("nhis", "국민건강보험공단"),
                new CompanyDto("lh", "한국토지주택공사 (LH)")
        );
    }

    /**
     * 채용공고 가산점 분석 로직
     */
    public List<AlioAnalysisResponse> analyzeJobs(AlioAnalysisRequest request) {
        List<AlioAnalysisResponse> resultList = new ArrayList<>();
        List<Map<String, String>> sampleJobs = getSampleJobs();

        String userRegion = request.getUserRegion() != null ? request.getUserRegion() : "전북";
        List<String> userCerts = request.getUserCertificates() != null ? request.getUserCertificates() : Collections.emptyList();

        for (Map<String, String> job : sampleJobs) {
            String fullContent = job.get("title") + " " + job.get("content");

            List<String> matchedCerts = new ArrayList<>();
            int certScore = 0;

            for (String cert : userCerts) {
                if (Pattern.compile(cert, Pattern.CASE_INSENSITIVE).matcher(fullContent).find()) {
                    matchedCerts.add(cert);
                    certScore += 3;
                }
            }

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
        job1.put("title", "2026년도 신입직원(행정/전산/데이터) 공개채용 공고");
        job1.put("location", "전북 전주시");
        job1.put("content", "우대사항: SQLD, ADsP, 정보처리기사 보유자. 전북지역 대학 졸업자(이전지역인재) 가점 부여.");
        job1.put("attachmentUrl", "https://example.com/nps.pdf");
        jobs.add(job1);

        Map<String, String> job2 = new HashMap<>();
        job2.put("id", "JOB_LX_01");
        job2.put("companyName", "LX 한국국토정보공사");
        job2.put("title", "2026년 상반기 공간정보 및 IT 정규직 채용");
        job2.put("location", "전북 전주시");
        job2.put("content", "필수/우대자격증: SQLD, ADsP, 컴퓨터활용능력 1급. 이전지역인재 우대.");
        job2.put("attachmentUrl", "https://example.com/lx.hwpx");
        jobs.add(job2);

        Map<String, String> job3 = new HashMap<>();
        job3.put("id", "JOB_KEPCO_01");
        job3.put("companyName", "한국전력공사");
        job3.put("title", "2026년 대졸수준 신입사원 공채");
        job3.put("location", "전남 나주시");
        job3.put("content", "우대자격증: 정보처리기사, 컴퓨터활용능력 1급 보유자. 이전지역인재 우대.");
        job3.put("attachmentUrl", "https://example.com/kepco.pdf");
        jobs.add(job3);

        return jobs;
    }
}
