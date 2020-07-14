package com.google.sps.servlet;

import static org.junit.Assert.assertEquals;

import com.google.sps.AssociationAnalysis;
import com.google.sps.AssociationResult;
import com.google.sps.EntitySentiment;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.mockito.Mockito;

public class AssociationAnalysisTest extends Mockito {

  @Test
  public void testEmpty() throws Exception {
    AssociationAnalysis analysis = new AssociationAnalysis();
    ArrayList<AssociationResult> res = analysis.calculateScores(new ArrayList<EntitySentiment>());
    assertEquals(res, new ArrayList<AssociationResult>());
  }

  @Test
  public void testAllDistinct() {
    ArrayList<EntitySentiment> input =
        new ArrayList<EntitySentiment>(
            Arrays.asList(
                new EntitySentiment("test1", 0.5f, 0.5f),
                new EntitySentiment("test2", 0.9f, -0.6f),
                new EntitySentiment("test3", 0f, 1.0f)));
    AssociationAnalysis analysis = new AssociationAnalysis();
    ArrayList<AssociationResult> res = analysis.calculateScores(input);
    assertEquals(
        res,
        new ArrayList(
            Arrays.asList(
                new AssociationResult("test2", -0.54f),
                new AssociationResult("test3", -0f),
                new AssociationResult("test1", 0.25f))));
  }

  @Test
  public void testCombineTogether() {
    ArrayList<EntitySentiment> input =
        new ArrayList<EntitySentiment>(
            Arrays.asList(
                new EntitySentiment("test1", 0.5f, 0.5f),
                new EntitySentiment("test1", 0.9f, 0.6f),
                new EntitySentiment("test2", 0f, 1.0f),
                new EntitySentiment("test2", 1.0f, -1.0f)));
    AssociationAnalysis analysis = new AssociationAnalysis();
    ArrayList<AssociationResult> res = analysis.calculateScores(input);
    assertEquals(
        res,
        new ArrayList(
            Arrays.asList(
                new AssociationResult("test2", -1.0f), new AssociationResult("test1", 0.79f))));
  }

  @Test
  public void testCombineSpreadOut() {
    ArrayList<EntitySentiment> input =
        new ArrayList<EntitySentiment>(
            Arrays.asList(
                new EntitySentiment("test1", 0.5f, 0.5f),
                new EntitySentiment("test2", 0.9f, 0.6f),
                new EntitySentiment("test1", 0f, 1.0f),
                new EntitySentiment("test2", 1.0f, 1.0f),
                new EntitySentiment("test1", 0.4f, 0.1f)));
    AssociationAnalysis analysis = new AssociationAnalysis();
    ArrayList<AssociationResult> res = analysis.calculateScores(input);
    assertEquals(
        res,
        new ArrayList(
            Arrays.asList(
                new AssociationResult("test1", 0.29f), new AssociationResult("test2", 1.54f))));
  }

  @Test
  public void testAllSame() {
    ArrayList<EntitySentiment> input =
        new ArrayList<EntitySentiment>(
            Arrays.asList(
                new EntitySentiment("test1", 0.5f, 0.5f),
                new EntitySentiment("test1", 0.9f, 0.6f),
                new EntitySentiment("test1", 0f, 1.0f),
                new EntitySentiment("test1", 1.0f, 1.0f),
                new EntitySentiment("test1", 0.4f, 0.1f)));
    AssociationAnalysis analysis = new AssociationAnalysis();
    ArrayList<AssociationResult> res = analysis.calculateScores(input);
    assertEquals(res, new ArrayList(Arrays.asList(new AssociationResult("test1", 1.83f))));
  }

  @Test
  public void testMixedSentiments() {
    ArrayList<EntitySentiment> input =
        new ArrayList<EntitySentiment>(
            Arrays.asList(
                new EntitySentiment("test1", 0.5f, -0.5f),
                new EntitySentiment("test2", 0.9f, 0.6f),
                new EntitySentiment("test1", 0f, 1.0f),
                new EntitySentiment("test2", 1.0f, -1.0f),
                new EntitySentiment("test3", 0.4f, 0.1f)));
    AssociationAnalysis analysis = new AssociationAnalysis();
    ArrayList<AssociationResult> res = analysis.calculateScores(input);
    assertEquals(
        res,
        new ArrayList(
            Arrays.asList(
                new AssociationResult("test2", -0.46f),
                new AssociationResult("test1", -0.25f),
                new AssociationResult("test3", 0.04f))));
  }

  @Test(expected = NullPointerException.class)
  public void testNullExceptionThrown() {
    AssociationAnalysis analysis = new AssociationAnalysis();
    analysis.calculateScores(null);
  }
}
