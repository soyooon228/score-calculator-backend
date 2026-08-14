package com.example.scorecalculator.repository;

import com.example.scorecalculator.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
