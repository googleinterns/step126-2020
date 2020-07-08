package com.google.sps.data;

import java.util.Date;

/* This object represents each response a user submits */
public class SurveyResponse {
  private String id;
  private Date date;
  private String completion;
  private String gender;
  private String ageRange;
  private String answerOne;
  private String answerTwo;
  private double score;
  private long responseTimeOne;
  private long responseTimeTwo;
  private long responseTimeThree;

  public void setId(String id) {
    this.id = id;
  }

  public void setCompletion(String completion) {
    this.completion = completion;
  }

  public void setAnswerOne(String answerOne) {
    this.answerOne = answerOne;
  }

  public void setAnswerTwo(String answerTwo) {
    this.answerTwo = answerTwo;
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

  public void setResponseTimeOne(long responseTime) {
    this.responseTimeOne = responseTime;
  }

  public void setResponseTimeTwo(long responseTime) {
    this.responseTimeTwo = responseTime;
  }

  public void setResponseTimeThree(long responseTime) {
    this.responseTimeThree = responseTime;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getId() {
    return id;
  }

  public String getCompletion() {
    return completion;
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

  public String getAnswerOne() {
    return answerOne;
  }

  public String getAnswerTwo() {
    return answerTwo;
  }

  public long getResponseTimeOne() {
    return responseTimeOne;
  }

  public long getResponseTimeTwo() {
    return responseTimeTwo;
  }

  public long getResponseTimeThree() {
    return responseTimeThree;
  }

  public Date getDate() {
    return date;
  }
}
