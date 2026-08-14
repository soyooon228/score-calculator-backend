package com.example.scorecalculator.dto;

import java.util.List;

public class AlioAnalysisResponse {
    private String id;
    private String companyName;
    private String title;
    private String location;
    private String attachmentUrl;
    private List<String> matchedCerts;
    private int certScore;
    private boolean isRegionalMatch;
    private int regionalScore;
    private int totalScore;

    // 생성자, Getters and Setters
    public AlioAnalysisResponse() {}

    public AlioAnalysisResponse(String id, String companyName, String title, String location, 
                                String attachmentUrl, List<String> matchedCerts, int certScore, 
                                boolean isRegionalMatch, int regionalScore, int totalScore) {
        this.id = id;
        this.companyName = companyName;
        this.title = title;
        this.location = location;
        this.attachmentUrl = attachmentUrl;
        this.matchedCerts = matchedCerts;
        this.certScore = certScore;
        this.isRegionalMatch = isRegionalMatch;
        this.regionalScore = regionalScore;
        this.totalScore = totalScore;
    }

    // Getters / Setters 생략 (IDE의 Generate 기능 이용 가능)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    public List<String> getMatchedCerts() { return matchedCerts; }
    public void setMatchedCerts(List<String> matchedCerts) { this.matchedCerts = matchedCerts; }
    public int getCertScore() { return certScore; }
    public void setCertScore(int certScore) { this.certScore = certScore; }
    public boolean isRegionalMatch() { return isRegionalMatch; }
    public void setRegionalMatch(boolean regionalMatch) { isRegionalMatch = regionalMatch; }
    public int getRegionalScore() { return regionalScore; }
    public void setRegionalScore(int regionalScore) { this.regionalScore = regionalScore; }
    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
}
