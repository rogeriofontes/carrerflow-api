package com.careerflow.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

@FeignClient(name = "openai", url = "${careerflow.openai.base-url}")
public interface OpenAiClient {

    @PostMapping("/chat/completions")
    OpenAiResponse chatCompletion(
            @RequestHeader("Authorization") String authorization,
            @RequestBody OpenAiRequest request
    );

    record OpenAiRequest(String model, List<Message> messages, double temperature) {
    }

    record Message(String role, String content) {
    }

    record OpenAiResponse(List<Choice> choices) {
    }

    record Choice(Message message) {
    }
}
