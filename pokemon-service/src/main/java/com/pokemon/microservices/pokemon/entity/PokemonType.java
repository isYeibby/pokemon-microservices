package com.pokemon.microservices.pokemon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pokemon_types")
public class PokemonType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Type name is required")
    @Size(max = 50, message = "Type name must not exceed 50 characters")
    @Column(unique = true, nullable = false)
    private String name;

    @Size(max = 20, message = "Color must not exceed 20 characters")
    @Column(length = 20)
    private String color;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // One-to-Many relationship with Pokemon (primary type)
    @OneToMany(mappedBy = "primaryType", fetch = FetchType.LAZY)
    private List<Pokemon> pokemonPrimaryType;

    // One-to-Many relationship with Pokemon (secondary type)
    @OneToMany(mappedBy = "secondaryType", fetch = FetchType.LAZY)
    private List<Pokemon> pokemonSecondaryType;

    // Constructors
    public PokemonType() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PokemonType(String name, String color) {
        this();
        this.name = name;
        this.color = color;
    }

    public PokemonType(String name, String color, String description) {
        this(name, color);
        this.description = description;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Pokemon> getPokemonPrimaryType() {
        return pokemonPrimaryType;
    }

    public void setPokemonPrimaryType(List<Pokemon> pokemonPrimaryType) {
        this.pokemonPrimaryType = pokemonPrimaryType;
    }

    public List<Pokemon> getPokemonSecondaryType() {
        return pokemonSecondaryType;
    }

    public void setPokemonSecondaryType(List<Pokemon> pokemonSecondaryType) {
        this.pokemonSecondaryType = pokemonSecondaryType;
    }

    // toString method
    @Override
    public String toString() {
        return "PokemonType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokemonType that)) return false;
        return name != null && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}