package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.InterviewRequest;
import com.careerflow.application.dto.InterviewResponse;
import com.careerflow.application.services.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<InterviewResponse> create(@Valid @RequestBody InterviewRequest request,
                                                    UriComponentsBuilder uriBuilder) {
        InterviewResponse response = interviewService.create(request);
        return ResponseEntity
                .created(uriBuilder.path("/api/interviews/{id}").buildAndExpand(response.id()).toUri())
                .body(response);
    }

    @GetMapping
    public List<InterviewResponse> findAll(@RequestParam(required = false) UUID jobApplicationId) {
        return interviewService.findAll(jobApplicationId);
    }

    @GetMapping("/{id}")
    public InterviewResponse findById(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        return interviewService.findById(id);
    }

    @PutMapping("/{id}")
    public InterviewResponse update(@PathVariable UUID id, @Valid @RequestBody InterviewRequest request) throws ChangeSetPersister.NotFoundException {
        return interviewService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        interviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
