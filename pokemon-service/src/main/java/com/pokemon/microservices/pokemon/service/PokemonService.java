package com.pokemon.microservices.pokemon.service;

import com.pokemon.microservices.pokemon.entity.Pokemon;
import com.pokemon.microservices.pokemon.entity.PokemonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PokemonService {

    // CRUD Operations
    Pokemon createPokemon(Pokemon pokemon);
    Pokemon updatePokemon(Long id, Pokemon pokemon);
    Optional<Pokemon> findById(Long id);
    Optional<Pokemon> findByPokedexNumber(Integer pokedexNumber);
    Optional<Pokemon> findByName(String name);
    Page<Pokemon> findAll(Pageable pageable);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsByPokedexNumber(Integer pokedexNumber);

    // Search and Filter Operations
    Page<Pokemon> searchPokemon(String searchTerm, Pageable pageable);
    List<Pokemon> findByType(PokemonType type);
    List<Pokemon> findByGeneration(Integer generation);
    List<Pokemon> findLegendaryPokemon();
    List<Pokemon> findMythicalPokemon();
    Page<Pokemon> findWithFilters(Integer generation, PokemonType primaryType,
                                  Boolean isLegendary, Integer minPokedexNumber,
                                  Integer maxPokedexNumber, Pageable pageable);

    // Evolution Operations
    List<Pokemon> getEvolutionChain(Long pokemonId);
    Pokemon evolvePokemon(Long pokemonId);
    boolean canEvolve(Long pokemonId);
    List<Pokemon> getBaseForms();
    List<Pokemon> getFinalForms();

    // Battle and Stats Operations
    List<Pokemon> findByMinTotalStats(Integer minTotal);
    List<Pokemon> getFastestPokemon(Integer minSpeed);
    List<Pokemon> getStrongestPokemon(Integer minAttack);
    Double calculateBattlePower(Long pokemonId);
    String comparePokemon(Long pokemon1Id, Long pokemon2Id);

    // Statistics Operations
    Long getTotalPokemonCount();
    Long getCountByGeneration(Integer generation);
    Long getCountByType(PokemonType type);
    Double getAverageTotalStats();

    // Validation Operations
    void validatePokemonData(Pokemon pokemon);
    void validateUniqueConstraints(Pokemon pokemon, Long excludeId);
}