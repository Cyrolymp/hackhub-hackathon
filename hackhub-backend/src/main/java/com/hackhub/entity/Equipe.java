package com.hackhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    private String specialite;

    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Integer tailleMax = 4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_id", nullable = false)
    private Utilisateur chef;

    @OneToMany(mappedBy = "equipe", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Utilisateur> membres = new ArrayList<>();

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Inscription> inscriptions = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public int getNombreMembres() {
        return membres == null ? 0 : membres.size();
    }
}
