package com.google.sps.data;

public class SurveyResponse {
    private String id;
    private double score;
    private String gender;
    private String ageRange;
    private long responseTime;
    private String date; 
    
    public SurveyResponse(String id, double score, String gender, String ageRange, long responseTime, String date) {
        this.id = id;
        this.score = score;
        this.gender = gender;
        this.ageRange = ageRange;
        this.responseTime = responseTime;
        this.date = date;
    }

    public String getId(){return this.id;}

    public double getScore(){return this.score;}

    public String gender(){return this.gender;}

    public String ageRange(){return this.ageRange;}

    public long getResponseTime(){return this.responseTime;}

    public String getDate(){return this.date;}
}