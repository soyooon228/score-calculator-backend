package com.example.scorecalculator.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 예: SQLD, ADsP, 정보처리기사, 한국사능력검정 1급

    @Column(nullable = false)
    private String category; // 예: DATA, IT, LANGUAGE, HISTORY
}
