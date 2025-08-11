package com.pokemon.microservices.pokemon.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "pokemon")
@Data
@EqualsAndHashCode(exclude = {"primaryType", "secondaryType", "evolvesFrom", "evolvesTo"})
@ToString(exclude = {"primaryType", "secondaryType", "evolvesFrom", "evolvesTo"})
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "pokedex_number", unique = true, nullable = false)
    private Integer pokedexNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double height;
    private Double weight;

    // Cambiar a EAGER para evitar problemas de proxy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_type_id")
    @JsonIgnoreProperties({"pokemonPrimaryType", "pokemonSecondaryType"})
    private PokemonType primaryType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "secondary_type_id")
    @JsonIgnoreProperties({"pokemonPrimaryType", "pokemonSecondaryType"})
    private PokemonType secondaryType;

    // Stats
    private Integer hp;
    private Integer attack;
    private Integer defense;
    @Column(name = "special_attack")
    private Integer specialAttack;
    @Column(name = "special_defense")
    private Integer specialDefense;
    private Integer speed;

    // Metadata
    @Column(name = "is_legendary")
    private Boolean isLegendary = false;
    @Column(name = "is_mythical")
    private Boolean isMythical = false;
    private Integer generation;
    @Column(name = "image_url")
    private String imageUrl;

    // Evolution relationships - mantener LAZY pero con JsonIgnoreProperties
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evolves_from_id")
    @JsonIgnoreProperties({"evolvesFrom", "evolvesTo", "primaryType", "secondaryType"})
    private Pokemon evolvesFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evolves_to_id")
    @JsonIgnoreProperties({"evolvesFrom", "evolvesTo", "primaryType", "secondaryType"})
    private Pokemon evolvesTo;

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

    // ========== MÉTODOS CALCULADOS ==========

    public Map<String, Integer> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("hp", this.hp != null ? this.hp : 0);
        stats.put("attack", this.attack != null ? this.attack : 0);
        stats.put("defense", this.defense != null ? this.defense : 0);
        stats.put("specialAttack", this.specialAttack != null ? this.specialAttack : 0);
        stats.put("specialDefense", this.specialDefense != null ? this.specialDefense : 0);
        stats.put("speed", this.speed != null ? this.speed : 0);
        return stats;
    }

    public Integer getTotalStats() {
        return (hp != null ? hp : 0) +
                (attack != null ? attack : 0) +
                (defense != null ? defense : 0) +
                (specialAttack != null ? specialAttack : 0) +
                (specialDefense != null ? specialDefense : 0) +
                (speed != null ? speed : 0);
    }

    public Double getBMI() {
        if (height == null || weight == null || height == 0) {
            return 0.0;
        }
        return weight / (height * height);
    }

    public Boolean canEvolve() {
        return this.evolvesTo != null;
    }

    public Boolean isSpecial() {
        return (isLegendary != null && isLegendary) || (isMythical != null && isMythical);
    }

    public Integer getEvolutionLevel() {
        if (evolvesFrom == null) {
            return 1; // Primera evolución
        } else if (evolvesTo == null) {
            return 3; // Evolución final
        } else {
            return 2; // Evolución intermedia
        }
    }
}