package com.careerflow.application.gateways;

import com.careerflow.application.dto.StarEvaluationResponse;
import com.careerflow.domain.entities.Challenge;
import com.careerflow.domain.entities.Submission;

public interface AiEvaluationGateway {

    StarEvaluationResponse evaluate(Submission submission, Challenge challenge);
}
