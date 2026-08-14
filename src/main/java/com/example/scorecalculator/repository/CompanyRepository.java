package com.example.scorecalculator.repository;

import com.example.scorecalculator.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
