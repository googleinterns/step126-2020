package com.google.sps.data;

public class SurveyResponse {
    private String id;
    private double score;
    private String gender;
    private String ageRange;
    private long responseTime;
    private String date; 
    private long day;
    
    /* This object represents each response a user submits */
    public SurveyResponse(String id, double score, String gender, String ageRange, long responseTime, String date, long day) {
        this.id = id;
        this.score = score;
        this.gender = gender;
        this.ageRange = ageRange;
        this.responseTime = responseTime;
        this.date = date;
        this.day = day;
    }

    public String getId() {return id;}

    public double getScore() {return score;}

    public String getGender() {return gender;}

    public String getAgeRange() {return ageRange;}

    public long getResponseTime() {return responseTime;}

    public String getDate() {return date;}

    public long getDay() {return day;}
}