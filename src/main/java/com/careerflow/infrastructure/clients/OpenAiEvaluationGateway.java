package com.careerflow.infrastructure.clients;

import com.careerflow.application.dto.StarEvaluationResponse;
import com.careerflow.application.gateways.AiEvaluationGateway;
import com.careerflow.domain.entities.Challenge;
import com.careerflow.domain.entities.Submission;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAiEvaluationGateway implements AiEvaluationGateway {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    @Value("${careerflow.openai.api-key}")
    private String apiKey;

    @Value("${careerflow.openai.model}")
    private String model;

    @Override
    @CircuitBreaker(name = "openai", fallbackMethod = "fallbackEvaluate")
    @Retry(name = "openai")
    public StarEvaluationResponse evaluate(Submission submission, Challenge challenge) {
        String prompt = buildPrompt(submission, challenge);

        var request = new OpenAiClient.OpenAiRequest(
                model,
                List.of(
                        new OpenAiClient.Message("system", "You are an expert STAR method evaluator. " +
                                "Evaluate the submission and return a JSON object with: " +
                                "situationScore (0-10), taskScore (0-10), actionScore (0-10), resultScore (0-10), " +
                                "and feedback (string with detailed feedback in Portuguese). " +
                                "Return ONLY valid JSON, no markdown."),
                        new OpenAiClient.Message("user", prompt)
                ),
                0.3
        );

        var response = openAiClient.chatCompletion("Bearer " + apiKey, request);
        String content = response.choices().getFirst().message().content();

        return parseResponse(content, submission);
    }

    private String buildPrompt(Submission submission, Challenge challenge) {
        return """
                Evaluate the following STAR submission for the challenge:
                
                Challenge: %s
                Description: %s
                Required Skills: %s
                Difficulty: %s
                
                STAR Submission:
                - Situation: %s
                - Task: %s
                - Action: %s
                - Result: %s
                
                Score each component from 0 to 10 based on clarity, relevance, specificity, and impact.
                """.formatted(
                challenge.getTitle(),
                challenge.getDescription(),
                String.join(", ", challenge.getSkills()),
                challenge.getDifficulty(),
                submission.getSituation(),
                submission.getTask(),
                submission.getAction(),
                submission.getResult()
        );
    }

    private StarEvaluationResponse parseResponse(String content, Submission submission) {
        try {
            String cleaned = content.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
            JsonNode node = objectMapper.readTree(cleaned);

            double situationScore = node.get("situationScore").asDouble();
            double taskScore = node.get("taskScore").asDouble();
            double actionScore = node.get("actionScore").asDouble();
            double resultScore = node.get("resultScore").asDouble();
            String feedback = node.get("feedback").asText();

            double finalScore = (situationScore * 0.2) + (taskScore * 0.2)
                    + (actionScore * 0.3) + (resultScore * 0.3);

            return new StarEvaluationResponse(
                    null, submission.getId(),
                    situationScore, taskScore, actionScore, resultScore,
                    Math.round(finalScore * 100.0) / 100.0,
                    feedback, null
            );
        } catch (Exception e) {
            log.error("Failed to parse OpenAI response: {}", content, e);
            return fallbackScore(submission);
        }
    }

    @SuppressWarnings("unused")
    private StarEvaluationResponse fallbackEvaluate(Submission submission, Challenge challenge, Throwable t) {
        log.warn("OpenAI circuit breaker activated, using fallback evaluation", t);
        return fallbackScore(submission);
    }

    private StarEvaluationResponse fallbackScore(Submission submission) {
        return new StarEvaluationResponse(
                null, submission.getId(),
                5.0, 5.0, 5.0, 5.0, 5.0,
                "Avaliação automática indisponível. Score padrão atribuído. A avaliação será reprocessada em breve.",
                null
        );
    }
}
