package com.pokemon.microservices.pokemon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pokemon name is required")
    @Size(max = 100, message = "Pokemon name must not exceed 100 characters")
    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "pokedex_number", unique = true)
    @Min(value = 1, message = "Pokedex number must be positive")
    @Max(value = 9999, message = "Pokedex number must not exceed 9999")
    private Integer pokedexNumber;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @DecimalMin(value = "0.1", message = "Height must be positive")
    @DecimalMax(value = "999.9", message = "Height is too large")
    @Column(precision = 4, scale = 1)
    private Double height; // in meters

    @DecimalMin(value = "0.1", message = "Weight must be positive")
    @DecimalMax(value = "9999.9", message = "Weight is too large")
    @Column(precision = 5, scale = 1)
    private Double weight; // in kilograms

    // Type relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_type_id", nullable = false)
    @NotNull(message = "Primary type is required")
    private PokemonType primaryType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "secondary_type_id")
    private PokemonType secondaryType;

    // Stats embedded
    @Embedded
    private PokemonStats stats;

    // Evolution information
    @Column(name = "evolution_level")
    @Min(value = 1, message = "Evolution level must be positive")
    @Max(value = 100, message = "Evolution level must not exceed 100")
    private Integer evolutionLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evolves_from_id")
    private Pokemon evolvesFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evolves_to_id")
    private Pokemon evolvesTo;

    // Additional characteristics
    @Column(name = "is_legendary")
    private Boolean isLegendary = false;

    @Column(name = "is_mythical")
    private Boolean isMythical = false;

    @Column(name = "generation")
    @Min(value = 1, message = "Generation must be positive")
    @Max(value = 10, message = "Generation must not exceed 10")
    private Integer generation;

    @Size(max = 200, message = "Image URL must not exceed 200 characters")
    @Column(name = "image_url")
    private String imageUrl;

    // Timestamps
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Pokemon() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.stats = new PokemonStats();
        this.isLegendary = false;
        this.isMythical = false;
    }

    public Pokemon(String name, PokemonType primaryType) {
        this();
        this.name = name;
        this.primaryType = primaryType;
    }

    public Pokemon(String name, Integer pokedexNumber, PokemonType primaryType) {
        this(name, primaryType);
        this.pokedexNumber = pokedexNumber;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean isDualType() {
        return secondaryType != null;
    }

    public boolean canEvolve() {
        return evolvesTo != null;
    }

    public boolean isEvolution() {
        return evolvesFrom != null;
    }

    public boolean isSpecial() {
        return isLegendary || isMythical;
    }

    public double getBMI() {
        if (height != null && weight != null && height > 0) {
            return weight / (height * height);
        }
        return 0.0;
    }

    public int getTotalStats() {
        return stats != null ? stats.getTotal() : 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPokedexNumber() {
        return pokedexNumber;
    }

    public void setPokedexNumber(Integer pokedexNumber) {
        this.pokedexNumber = pokedexNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public PokemonType getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(PokemonType primaryType) {
        this.primaryType = primaryType;
    }

    public PokemonType getSecondaryType() {
        return secondaryType;
    }

    public void setSecondaryType(PokemonType secondaryType) {
        this.secondaryType = secondaryType;
    }

    public PokemonStats getStats() {
        return stats;
    }

    public void setStats(PokemonStats stats) {
        this.stats = stats;
    }

    public Integer getEvolutionLevel() {
        return evolutionLevel;
    }

    public void setEvolutionLevel(Integer evolutionLevel) {
        this.evolutionLevel = evolutionLevel;
    }

    public Pokemon getEvolvesFrom() {
        return evolvesFrom;
    }

    public void setEvolvesFrom(Pokemon evolvesFrom) {
        this.evolvesFrom = evolvesFrom;
    }

    public Pokemon getEvolvesTo() {
        return evolvesTo;
    }

    public void setEvolvesTo(Pokemon evolvesTo) {
        this.evolvesTo = evolvesTo;
    }

    public Boolean getIsLegendary() {
        return isLegendary;
    }

    public void setIsLegendary(Boolean legendary) {
        isLegendary = legendary;
    }

    public Boolean getIsMythical() {
        return isMythical;
    }

    public void setIsMythical(Boolean mythical) {
        isMythical = mythical;
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // toString method
    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pokedexNumber=" + pokedexNumber +
                ", primaryType=" + (primaryType != null ? primaryType.getName() : null) +
                ", secondaryType=" + (secondaryType != null ? secondaryType.getName() : null) +
                ", height=" + height +
                ", weight=" + weight +
                ", isLegendary=" + isLegendary +
                ", generation=" + generation +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pokemon pokemon)) return false;
        return pokedexNumber != null && pokedexNumber.equals(pokemon.pokedexNumber);
    }

    @Override
    public int hashCode() {
        return pokedexNumber != null ? pokedexNumber.hashCode() : 0;
    }
}