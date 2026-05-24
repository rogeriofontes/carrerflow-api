package com.careerflow.interfaces.rest;

import com.careerflow.application.dto.ResumeRequest;
import com.careerflow.application.dto.ResumeResponse;
import com.careerflow.application.services.ResumeService;
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
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResumeResponse> create(@Valid @RequestBody ResumeRequest request,
                                                 UriComponentsBuilder uriBuilder) {
        ResumeResponse response = resumeService.create(request);
        return ResponseEntity
                .created(uriBuilder.path("/api/resumes/{id}").buildAndExpand(response.id()).toUri())
                .body(response);
    }

    @GetMapping
    public List<ResumeResponse> findAll(@RequestParam(required = false) UUID userId) {
        return resumeService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ResumeResponse findById(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        return resumeService.findById(id);
    }

    @PutMapping("/{id}")
    public ResumeResponse update(@PathVariable UUID id, @Valid @RequestBody ResumeRequest request) throws ChangeSetPersister.NotFoundException {
        return resumeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        resumeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
