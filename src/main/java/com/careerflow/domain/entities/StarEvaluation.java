package com.careerflow.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "star_evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StarEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false, unique = true)
    private Submission submission;

    @Column(name = "situation_score", nullable = false)
    private Double situationScore;

    @Column(name = "task_score", nullable = false)
    private Double taskScore;

    @Column(name = "action_score", nullable = false)
    private Double actionScore;

    @Column(name = "result_score", nullable = false)
    private Double resultScore;

    @Column(name = "final_score", nullable = false)
    private Double finalScore;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "evaluated_at")
    @CreationTimestamp
    private LocalDateTime evaluatedAt;

    public static Double calculateFinalScore(Double situationScore, Double taskScore,
                                             Double actionScore, Double resultScore) {
        return (situationScore * 0.2) + (taskScore * 0.2) + (actionScore * 0.3) + (resultScore * 0.3);
    }
}
