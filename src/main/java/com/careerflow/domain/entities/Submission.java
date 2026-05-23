package com.careerflow.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String situation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String task;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String action;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String result;

    @Column(name = "submitted_at")
    @CreationTimestamp
    private LocalDateTime submittedAt;

    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StarEvaluation evaluation;
}
