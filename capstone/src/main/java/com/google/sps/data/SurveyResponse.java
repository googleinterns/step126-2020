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

  /**
   * Sets the unique id for the survey response
   *
   * @param id This is the unique id for each survey response
   * @return Void
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Sets the completion status
   *
   * @param completion This is the status of the survey: Complete or partial
   * @return Void
   */
  public void setCompletion(String completion) {
    this.completion = completion;
  }

  /**
   * Sets the response for survey question one
   *
   * @param answerOne This is the user's response to question one
   * @return Void
   */
  public void setAnswerOne(String answerOne) {
    this.answerOne = answerOne;
  }

  /**
   * Sets the response for survey question two
   *
   * @param answerTwo This is the user's response to question two
   * @return Void
   */
  public void setAnswerTwo(String answerTwo) {
    this.answerTwo = answerTwo;
  }

  /**
   * Sets the sentiment score for the survey response
   *
   * @param score This is the user's sentiment score (-1 to 1)
   * @return Void
   */
  public void setScore(double score) {
    this.score = score;
  }

  /**
   * Sets the gender of the user
   *
   * @param gender The user's gender
   * @return Void
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Sets the age group of the user
   *
   * @param ageRange The user's age range
   * @return Void
   */
  public void setAgeRange(String ageRange) {
    this.ageRange = ageRange;
  }

  /**
   * Sets the response response times for questions 1-3
   *
   * @param responseTimeOne This is the user's response time to question one
   * @param responseTimeTwo This is the user's response time to question two
   * @param responseTimeThree This is the user's response time to question three
   * @return Void
   */
  public void setResponseTimeOne(long responseTimeOne) {
    this.responseTimeOne = responseTimeOne;
  }

  public void setResponseTimeTwo(long responseTimeTwo) {
    this.responseTimeTwo = responseTimeTwo;
  }

  public void setResponseTimeThree(long responseTimeThree) {
    this.responseTimeThree = responseTimeThree;
  }

  /**
   * Sets the date the survey was taken
   *
   * @param date The date the user submitted the survey
   * @return Void
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Gets the id of the survey response
   *
   * @return String Id of the user
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the completion status of the survey
   *
   * @return String The completion status
   */
  public String getCompletion() {
    return completion;
  }

  /**
   * Gets the sentiment score of the response
   *
   * @return double The sentiment score (-1 to 1)
   */
  public double getScore() {
    return score;
  }

  /**
   * Gets the gender of the user
   *
   * @return String The gender of the user
   */
  public String getGender() {
    return gender;
  }

  /**
   * Gets the age group of the user
   *
   * @return String The age group of the user
   */
  public String getAgeRange() {
    return ageRange;
  }

  /**
   * Gets the first question response
   *
   * @return String The first question response
   */
  public String getAnswerOne() {
    return answerOne;
  }

  /**
   * Gets the second question response
   *
   * @return String The second question response
   */
  public String getAnswerTwo() {
    return answerTwo;
  }

  /**
   * Gets the response time for each question
   *
   * @return Long The response times for each question
   */
  public long getResponseTimeOne() {
    return responseTimeOne;
  }

  public long getResponseTimeTwo() {
    return responseTimeTwo;
  }

  public long getResponseTimeThree() {
    return responseTimeThree;
  }

  /**
   * Gets the date the survey was taken
   *
   * @return Date The date the survey was taken
   */
  public Date getDate() {
    return date;
  }
}
