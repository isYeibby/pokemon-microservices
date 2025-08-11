package com.pokemon.microservices.pokemon.repository;

import com.pokemon.microservices.pokemon.entity.Pokemon;
import com.pokemon.microservices.pokemon.entity.PokemonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

    // Basic finders
    Optional<Pokemon> findByPokedexNumber(Integer pokedexNumber);
    Optional<Pokemon> findByNameIgnoreCase(String name);

    // Existence checks
    boolean existsByNameIgnoreCase(String name);
    boolean existsByPokedexNumber(Integer pokedexNumber);

    // Type-based queries
    List<Pokemon> findByPrimaryType(PokemonType primaryType);
    List<Pokemon> findBySecondaryType(PokemonType secondaryType);
    List<Pokemon> findByPrimaryTypeOrSecondaryType(PokemonType type1, PokemonType type2);

    // Generation and special Pokemon
    List<Pokemon> findByGeneration(Integer generation);
    List<Pokemon> findByIsLegendaryTrue();
    List<Pokemon> findByIsMythicalTrue();
    List<Pokemon> findByIsLegendaryTrueOrIsMythicalTrue();

    // Evolution queries
    List<Pokemon> findByEvolvesFromIsNull(); // Base forms (no pre-evolution)
    List<Pokemon> findByEvolvesToIsNull(); // Final forms (no evolution)
    List<Pokemon> findByEvolvesFromIsNotNull(); // Evolved forms

    // Search and filtering
    @Query("SELECT p FROM Pokemon p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Pokemon> searchByNameOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT p FROM Pokemon p WHERE p.name LIKE %:name%")
    List<Pokemon> findByNameContainingIgnoreCase(@Param("name") String name);

    // Stats-based queries
    @Query("SELECT p FROM Pokemon p WHERE " +
            "(p.stats.hp + p.stats.attack + p.stats.defense + " +
            "p.stats.specialAttack + p.stats.specialDefense + p.stats.speed) >= :minTotal")
    List<Pokemon> findByMinTotalStats(@Param("minTotal") Integer minTotal);

    @Query("SELECT p FROM Pokemon p WHERE p.stats.speed >= :minSpeed ORDER BY p.stats.speed DESC")
    List<Pokemon> findFastestPokemon(@Param("minSpeed") Integer minSpeed);

    @Query("SELECT p FROM Pokemon p WHERE p.stats.attack >= :minAttack ORDER BY p.stats.attack DESC")
    List<Pokemon> findStrongestPokemon(@Param("minAttack") Integer minAttack);

    // Size-based queries
    List<Pokemon> findByHeightGreaterThanEqual(Double minHeight);
    List<Pokemon> findByWeightGreaterThanEqual(Double minWeight);

    // Advanced filtering
    @Query("SELECT p FROM Pokemon p WHERE " +
            "(:generation IS NULL OR p.generation = :generation) AND " +
            "(:primaryType IS NULL OR p.primaryType = :primaryType) AND " +
            "(:isLegendary IS NULL OR p.isLegendary = :isLegendary) AND " +
            "(:minPokedexNumber IS NULL OR p.pokedexNumber >= :minPokedexNumber) AND " +
            "(:maxPokedexNumber IS NULL OR p.pokedexNumber <= :maxPokedexNumber)")
    Page<Pokemon> findWithFilters(
            @Param("generation") Integer generation,
            @Param("primaryType") PokemonType primaryType,
            @Param("isLegendary") Boolean isLegendary,
            @Param("minPokedexNumber") Integer minPokedexNumber,
            @Param("maxPokedexNumber") Integer maxPokedexNumber,
            Pageable pageable);

    // Statistics queries
    @Query("SELECT COUNT(p) FROM Pokemon p WHERE p.generation = :generation")
    Long countByGeneration(@Param("generation") Integer generation);

    @Query("SELECT COUNT(p) FROM Pokemon p WHERE p.primaryType = :type OR p.secondaryType = :type")
    Long countByType(@Param("type") PokemonType type);

    @Query("SELECT AVG(p.stats.hp + p.stats.attack + p.stats.defense + " +
            "p.stats.specialAttack + p.stats.specialDefense + p.stats.speed) FROM Pokemon p")
    Double getAverageTotalStats();
}