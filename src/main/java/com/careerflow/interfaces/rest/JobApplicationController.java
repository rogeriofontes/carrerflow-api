package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.JobApplicationRequest;
import com.careerflow.application.dto.JobApplicationResponse;
import com.careerflow.application.services.JobApplicationService;
import com.careerflow.domain.entities.ApplicationStatus;
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
@RequestMapping("/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<JobApplicationResponse> create(@Valid @RequestBody JobApplicationRequest request,
                                                         UriComponentsBuilder uriBuilder) {
        JobApplicationResponse response = jobApplicationService.create(request);
        return ResponseEntity
                .created(uriBuilder.path("/api/job-applications/{id}").buildAndExpand(response.id()).toUri())
                .body(response);
    }

    @GetMapping
    public List<JobApplicationResponse> findAll(@RequestParam(required = false) UUID userId,
                                                @RequestParam(required = false) ApplicationStatus status) {
        return jobApplicationService.findAll(userId, status);
    }

    @GetMapping("/{id}")
    public JobApplicationResponse findById(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        return jobApplicationService.findById(id);
    }

    @PutMapping("/{id}")
    public JobApplicationResponse update(@PathVariable UUID id,
                                         @Valid @RequestBody JobApplicationRequest request) throws ChangeSetPersister.NotFoundException {
        return jobApplicationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        jobApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
