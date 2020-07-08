package com.google.sps.data;

/* This object represents each response a user submits */
public class SurveyResponse {
  private String id;
  private double score;
  private String gender;
  private String ageRange;
  private long responseTime;
  private String date;
  private long day;

  public void setId(String id) {
    this.id = id;
  }

  public void setScore(double score) {
    this.score = score;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void setAgeRange(String ageRange) {
    this.ageRange = ageRange;
  }

  public void setResponseTime(long responseTime) {
    this.responseTime = responseTime;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setDay(long day) {
    this.day = day;
  }

  public String getId() {
    return id;
  }

  public double getScore() {
    return score;
  }

  public String getGender() {
    return gender;
  }

  public String getAgeRange() {
    return ageRange;
  }

  public long getResponseTime() {
    return responseTime;
  }

  public String getDate() {
    return date;
  }

  public long getDay() {
    return day;
  }
}
