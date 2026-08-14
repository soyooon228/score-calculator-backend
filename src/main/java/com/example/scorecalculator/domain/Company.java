package com.example.scorecalculator.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 예: 국민연금공단, LX한국국토정보공사, 한국전력공사

    private String region; // 예: 전북, 전남, 나주 등 (지역인재 판별용)
}
