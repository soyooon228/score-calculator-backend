package com.example.scorecalculator.dto;

import java.util.List;

public class AlioAnalysisRequest {
    private String userRegion;
    private List<String> userCertificates;

    // Getters and Setters
    public String getUserRegion() {
        return userRegion;
    }

    public void setUserRegion(String userRegion) {
        this.userRegion = userRegion;
    }

    public List<String> getUserCertificates() {
        return userCertificates;
    }

    public void setUserCertificates(List<String> userCertificates) {
        this.userCertificates = userCertificates;
    }
}
