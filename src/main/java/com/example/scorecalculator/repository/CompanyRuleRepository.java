package com.example.scorecalculator.repository;

import com.example.scorecalculator.domain.CompanyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRuleRepository extends JpaRepository<CompanyRule, Long> {

    // N+1 문제 방지를 위한 Fetch Join
    @Query("SELECT DISTINCT cr FROM CompanyRule cr " +
           "JOIN FETCH cr.company " +
           "LEFT JOIN FETCH cr.ruleDetails rd " +
           "LEFT JOIN FETCH rd.certificate")
    List<CompanyRule> findAllWithDetails();
}
