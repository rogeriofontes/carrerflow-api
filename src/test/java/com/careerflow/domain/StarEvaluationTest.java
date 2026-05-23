package com.careerflow.domain;

import com.careerflow.domain.entities.StarEvaluation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StarEvaluationTest {

    @Test
    void shouldCalculateFinalScoreCorrectly() {
        // score = (situation * 0.2) + (task * 0.2) + (action * 0.3) + (result * 0.3)
        Double result = StarEvaluation.calculateFinalScore(8.0, 7.0, 9.0, 8.5);
        // 8*0.2 + 7*0.2 + 9*0.3 + 8.5*0.3 = 1.6 + 1.4 + 2.7 + 2.55 = 8.25
        assertEquals(8.25, result, 0.01);
    }

    @Test
    void shouldCalculateFinalScoreWithZeros() {
        Double result = StarEvaluation.calculateFinalScore(0.0, 0.0, 0.0, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void shouldCalculateFinalScoreWithPerfectScores() {
        Double result = StarEvaluation.calculateFinalScore(10.0, 10.0, 10.0, 10.0);
        assertEquals(10.0, result, 0.01);
    }

    @Test
    void shouldWeightActionAndResultMoreThanSituationAndTask() {
        // Same total raw scores but different distribution
        Double scoreHighAction = StarEvaluation.calculateFinalScore(5.0, 5.0, 10.0, 10.0);
        Double scoreHighSituation = StarEvaluation.calculateFinalScore(10.0, 10.0, 5.0, 5.0);
        // High action: 5*0.2 + 5*0.2 + 10*0.3 + 10*0.3 = 1 + 1 + 3 + 3 = 8.0
        // High situation: 10*0.2 + 10*0.2 + 5*0.3 + 5*0.3 = 2 + 2 + 1.5 + 1.5 = 7.0
        assertEquals(8.0, scoreHighAction, 0.01);
        assertEquals(7.0, scoreHighSituation, 0.01);
    }
}
