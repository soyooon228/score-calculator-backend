package com.example.scorecalculator.dto;

import com.example.scorecalculator.domain.Certificate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CertificateResponse {
    private Long id;
    private String name;     // 예: SQLD, ADsP, 정보처리기사
    private String category; // 예: DATA, IT, HISTORY

    public static CertificateResponse from(Certificate certificate) {
        return CertificateResponse.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .category(certificate.getCategory())
                .build();
    }
}
