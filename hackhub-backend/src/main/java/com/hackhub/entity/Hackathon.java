package com.hackhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hackathons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    private String categorie;

    private String lieu;

    @NotNull
    private LocalDateTime dateDebut;

    @NotNull
    private LocalDateTime dateFin;

    @Column(nullable = false)
    @Builder.Default
    private Integer placesMax = 100;

    private String prixRecompense;

    @ElementCollection
    @CollectionTable(name = "hackathon_technologies", joinColumns = @JoinColumn(name = "hackathon_id"))
    @Column(name = "technologie")
    @Builder.Default
    private List<String> technologies = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutHackathon statut = StatutHackathon.OUVERT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisateur_id", nullable = false)
    private Utilisateur organisateur;

    @OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Inscription> inscriptions = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public int getNombreInscrits() {
        return inscriptions == null ? 0 : inscriptions.size();
    }

    public int getPlacesRestantes() {
        return placesMax - getNombreInscrits();
    }
}
