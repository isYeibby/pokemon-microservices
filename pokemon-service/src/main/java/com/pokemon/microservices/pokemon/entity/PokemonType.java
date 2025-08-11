package com.pokemon.microservices.pokemon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pokemon_types")
@Data
@EqualsAndHashCode(exclude = {"pokemonPrimaryType", "pokemonSecondaryType"})
@ToString(exclude = {"pokemonPrimaryType", "pokemonSecondaryType"})
public class PokemonType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String color;

    @Column(columnDefinition = "TEXT")
    private String description;

    // MANTENER @JsonIgnore para evitar ciclos infinitos
    @OneToMany(mappedBy = "primaryType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pokemon> pokemonPrimaryType;

    @OneToMany(mappedBy = "secondaryType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pokemon> pokemonSecondaryType;

    // Audit fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}