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

    // ========== BÚSQUEDAS BÁSICAS ==========

    Optional<Pokemon> findByNameIgnoreCase(String name);
    Optional<Pokemon> findByPokedexNumber(Integer pokedexNumber);

    // ========== BÚSQUEDAS POR TEXTO ==========

    List<Pokemon> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Pokemon p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Pokemon> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            @Param("searchTerm") String name, @Param("searchTerm") String description);

    @Query("SELECT p FROM Pokemon p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Pokemon> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            @Param("searchTerm") String name, @Param("searchTerm") String description, Pageable pageable);

    // ========== BÚSQUEDAS POR TIPO ==========

    @Query("SELECT p FROM Pokemon p WHERE p.primaryType.name = :typeName OR p.secondaryType.name = :typeName")
    List<Pokemon> findByPrimaryTypeNameIgnoreCaseOrSecondaryTypeNameIgnoreCase(
            @Param("typeName") String primaryTypeName, @Param("typeName") String secondaryTypeName);

    List<Pokemon> findByPrimaryTypeOrSecondaryType(PokemonType primaryType, PokemonType secondaryType);

    List<Pokemon> findByPrimaryType(PokemonType primaryType);
    List<Pokemon> findBySecondaryType(PokemonType secondaryType);

    // ========== BÚSQUEDAS POR GENERACIÓN ==========

    List<Pokemon> findByGeneration(Integer generation);
    Long countByGeneration(Integer generation);

    // ========== BÚSQUEDAS POR CARACTERÍSTICAS ==========

    List<Pokemon> findByIsLegendaryTrue();
    List<Pokemon> findByIsMythicalTrue();
    List<Pokemon> findByIsLegendaryTrueOrIsMythicalTrue();

    // ========== BÚSQUEDAS POR ESTADÍSTICAS ==========

    List<Pokemon> findByHpGreaterThanEqual(Integer hp);
    List<Pokemon> findByAttackGreaterThanEqual(Integer attack);
    List<Pokemon> findByDefenseGreaterThanEqual(Integer defense);
    List<Pokemon> findBySpecialAttackGreaterThanEqual(Integer specialAttack);
    List<Pokemon> findBySpecialDefenseGreaterThanEqual(Integer specialDefense);
    List<Pokemon> findBySpeedGreaterThanEqual(Integer speed);

    // ========== BÚSQUEDAS POR EVOLUCIÓN ==========

    List<Pokemon> findByEvolvesFromIsNull(); // Pokemon base (primera evolución)
    List<Pokemon> findByEvolvesToIsNull(); // Pokemon final (última evolución)
    List<Pokemon> findByEvolvesFromIsNotNull(); // Pokemon que evolucionan de otros
    List<Pokemon> findByEvolvesToIsNotNull(); // Pokemon que evolucionan a otros

    // ========== CONTEOS ==========

    @Query("SELECT COUNT(p) FROM Pokemon p WHERE p.primaryType = :type OR p.secondaryType = :type")
    Long countByPrimaryTypeOrSecondaryType(@Param("type") PokemonType primaryType, @Param("type") PokemonType secondaryType);

    @Query("SELECT COUNT(p) FROM Pokemon p WHERE p.primaryType = :type")
    Long countByPrimaryType(@Param("type") PokemonType type);

    @Query("SELECT COUNT(p) FROM Pokemon p WHERE p.secondaryType = :type")
    Long countBySecondaryType(@Param("type") PokemonType type);

    // ========== CONSULTAS PERSONALIZADAS ==========

    @Query("SELECT p FROM Pokemon p WHERE " +
            "(:generation IS NULL OR p.generation = :generation) AND " +
            "(:primaryType IS NULL OR p.primaryType = :primaryType) AND " +
            "(:isLegendary IS NULL OR p.isLegendary = :isLegendary) AND " +
            "(:minPokedex IS NULL OR p.pokedexNumber >= :minPokedex) AND " +
            "(:maxPokedex IS NULL OR p.pokedexNumber <= :maxPokedex)")
    Page<Pokemon> findWithFilters(@Param("generation") Integer generation,
                                  @Param("primaryType") PokemonType primaryType,
                                  @Param("isLegendary") Boolean isLegendary,
                                  @Param("minPokedex") Integer minPokedexNumber,
                                  @Param("maxPokedex") Integer maxPokedexNumber,
                                  Pageable pageable);

    @Query("SELECT p FROM Pokemon p ORDER BY (p.hp + p.attack + p.defense + p.specialAttack + p.specialDefense + p.speed) DESC")
    List<Pokemon> findTopByTotalStatsOrderByTotalStatsDesc(Pageable pageable);

    @Query("SELECT p FROM Pokemon p ORDER BY p.speed DESC")
    List<Pokemon> findTopBySpeedOrderBySpeedDesc(Pageable pageable);

    @Query("SELECT p FROM Pokemon p ORDER BY p.attack DESC")
    List<Pokemon> findTopByAttackOrderByAttackDesc(Pageable pageable);

    // ========== ESTADÍSTICAS ==========

    @Query("SELECT AVG(p.hp + p.attack + p.defense + p.specialAttack + p.specialDefense + p.speed) FROM Pokemon p")
    Double getAverageTotalStats();

    @Query("SELECT MAX(p.hp + p.attack + p.defense + p.specialAttack + p.specialDefense + p.speed) FROM Pokemon p")
    Integer getMaxTotalStats();

    @Query("SELECT MIN(p.hp + p.attack + p.defense + p.specialAttack + p.specialDefense + p.speed) FROM Pokemon p")
    Integer getMinTotalStats();
}