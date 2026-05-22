package com.hackhub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"equipe_id", "hackathon_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutInscription statut = StatutInscription.VALIDEE;

    private String commentaire;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateInscription;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
