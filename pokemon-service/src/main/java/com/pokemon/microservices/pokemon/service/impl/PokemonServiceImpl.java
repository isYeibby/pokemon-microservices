package com.pokemon.microservices.pokemon.service.impl;

import com.pokemon.microservices.pokemon.entity.Pokemon;
import com.pokemon.microservices.pokemon.entity.PokemonType;
import com.pokemon.microservices.pokemon.repository.PokemonRepository;
import com.pokemon.microservices.pokemon.service.PokemonService;
import com.pokemon.microservices.pokemon.service.PokemonTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PokemonServiceImpl implements PokemonService {

    private static final Logger logger = LoggerFactory.getLogger(PokemonServiceImpl.class);

    private final PokemonRepository pokemonRepository;
    private final PokemonTypeService pokemonTypeService;

    @Autowired
    public PokemonServiceImpl(PokemonRepository pokemonRepository,
                              PokemonTypeService pokemonTypeService) {
        this.pokemonRepository = pokemonRepository;
        this.pokemonTypeService = pokemonTypeService;
    }

    // CRUD Operations
    @Override
    public Pokemon createPokemon(Pokemon pokemon) {
        logger.info("Creating new Pokemon: {}", pokemon.getName());

        validatePokemonData(pokemon);
        validateUniqueConstraints(pokemon, null);

        Pokemon savedPokemon = pokemonRepository.save(pokemon);
        logger.info("Pokemon created successfully with ID: {}", savedPokemon.getId());

        return savedPokemon;
    }

    @Override
    public Pokemon updatePokemon(Long id, Pokemon pokemon) {
        logger.info("Updating Pokemon with ID: {}", id);

        Pokemon existingPokemon = pokemonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pokemon not found with ID: " + id));

        validatePokemonData(pokemon);
        validateUniqueConstraints(pokemon, id);

        // Update fields
        existingPokemon.setName(pokemon.getName());
        existingPokemon.setPokedexNumber(pokemon.getPokedexNumber());
        existingPokemon.setDescription(pokemon.getDescription());
        existingPokemon.setHeight(pokemon.getHeight());
        existingPokemon.setWeight(pokemon.getWeight());
        existingPokemon.setPrimaryType(pokemon.getPrimaryType());
        existingPokemon.setSecondaryType(pokemon.getSecondaryType());
        existingPokemon.setStats(pokemon.getStats());
        existingPokemon.setEvolutionLevel(pokemon.getEvolutionLevel());
        existingPokemon.setIsLegendary(pokemon.getIsLegendary());
        existingPokemon.setIsMythical(pokemon.getIsMythical());
        existingPokemon.setGeneration(pokemon.getGeneration());
        existingPokemon.setImageUrl(pokemon.getImageUrl());

        Pokemon updatedPokemon = pokemonRepository.save(existingPokemon);
        logger.info("Pokemon updated successfully: {}", updatedPokemon.getName());

        return updatedPokemon;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pokemon> findById(Long id) {
        logger.debug("Finding Pokemon by ID: {}", id);
        return pokemonRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pokemon> findByPokedexNumber(Integer pokedexNumber) {
        logger.debug("Finding Pokemon by Pokedex number: {}", pokedexNumber);
        return pokemonRepository.findByPokedexNumber(pokedexNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pokemon> findByName(String name) {
        logger.debug("Finding Pokemon by name: {}", name);
        return pokemonRepository.findByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pokemon> findAll(Pageable pageable) {
        logger.debug("Finding all Pokemon with pagination: {}", pageable);
        return pokemonRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting Pokemon with ID: {}", id);

        if (!pokemonRepository.existsById(id)) {
            throw new RuntimeException("Pokemon not found with ID: " + id);
        }

        pokemonRepository.deleteById(id);
        logger.info("Pokemon deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return pokemonRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return pokemonRepository.existsByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPokedexNumber(Integer pokedexNumber) {
        return pokemonRepository.existsByPokedexNumber(pokedexNumber);
    }

    // Search and Filter Operations
    @Override
    @Transactional(readOnly = true)
    public Page<Pokemon> searchPokemon(String searchTerm, Pageable pageable) {
        logger.debug("Searching Pokemon with term: {}", searchTerm);
        return pokemonRepository.searchByNameOrDescription(searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> findByType(PokemonType type) {
        logger.debug("Finding Pokemon by type: {}", type.getName());
        return pokemonRepository.findByPrimaryTypeOrSecondaryType(type, type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> findByGeneration(Integer generation) {
        logger.debug("Finding Pokemon by generation: {}", generation);
        return pokemonRepository.findByGeneration(generation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> findLegendaryPokemon() {
        logger.debug("Finding all legendary Pokemon");
        return pokemonRepository.findByIsLegendaryTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> findMythicalPokemon() {
        logger.debug("Finding all mythical Pokemon");
        return pokemonRepository.findByIsMythicalTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pokemon> findWithFilters(Integer generation, PokemonType primaryType,
                                         Boolean isLegendary, Integer minPokedexNumber,
                                         Integer maxPokedexNumber, Pageable pageable) {
        logger.debug("Finding Pokemon with filters - Generation: {}, Type: {}, Legendary: {}",
                generation, primaryType != null ? primaryType.getName() : null, isLegendary);

        return pokemonRepository.findWithFilters(generation, primaryType, isLegendary,
                minPokedexNumber, maxPokedexNumber, pageable);
    }

    // Evolution Operations
    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> getEvolutionChain(Long pokemonId) {
        logger.debug("Getting evolution chain for Pokemon ID: {}", pokemonId);

        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new RuntimeException("Pokemon not found with ID: " + pokemonId));

        List<Pokemon> evolutionChain = new ArrayList<>();

        // Find the base form
        Pokemon basePokemon = pokemon;
        while (basePokemon.getEvolvesFrom() != null) {
            basePokemon = basePokemon.getEvolvesFrom();
        }

        // Build the chain from base form
        Pokemon current = basePokemon;
        evolutionChain.add(current);

        while (current.getEvolvesTo() != null) {
            current = current.getEvolvesTo();
            evolutionChain.add(current);
        }

        return evolutionChain;
    }

    @Override
    public Pokemon evolvePokemon(Long pokemonId) {
        logger.info("Attempting to evolve Pokemon with ID: {}", pokemonId);

        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new RuntimeException("Pokemon not found with ID: " + pokemonId));

        if (!canEvolve(pokemonId)) {
            throw new RuntimeException("Pokemon cannot evolve: " + pokemon.getName());
        }

        // Evolution logic - for now, just return the evolution
        // In a real implementation, this might create a new Pokemon instance
        // or update the current one based on business rules

        Pokemon evolution = pokemon.getEvolvesTo();
        logger.info("Pokemon {} evolved to {}", pokemon.getName(), evolution.getName());

        return evolution;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canEvolve(Long pokemonId) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new RuntimeException("Pokemon not found with ID: " + pokemonId));

        return pokemon.canEvolve();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> getBaseForms() {
        logger.debug("Finding all base form Pokemon");
        return pokemonRepository.findByEvolvesFromIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> getFinalForms() {
        logger.debug("Finding all final form Pokemon");
        return pokemonRepository.findByEvolvesToIsNull();
    }

    // Battle and Stats Operations
    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> findByMinTotalStats(Integer minTotal) {
        logger.debug("Finding Pokemon with minimum total stats: {}", minTotal);
        return pokemonRepository.findByMinTotalStats(minTotal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> getFastestPokemon(Integer minSpeed) {
        logger.debug("Finding fastest Pokemon with minimum speed: {}", minSpeed);
        return pokemonRepository.findFastestPokemon(minSpeed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pokemon> getStrongestPokemon(Integer minAttack) {
        logger.debug("Finding strongest Pokemon with minimum attack: {}", minAttack);
        return pokemonRepository.findStrongestPokemon(minAttack);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateBattlePower(Long pokemonId) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new RuntimeException("Pokemon not found with ID: " + pokemonId));

        // Battle power calculation formula
        double basePower = pokemon.getTotalStats();
        double heightWeightBonus = (pokemon.getHeight() != null && pokemon.getWeight() != null)
                ? pokemon.getBMI() * 0.1 : 0;
        double specialBonus = pokemon.isSpecial() ? basePower * 0.2 : 0;

        double battlePower = basePower + heightWeightBonus + specialBonus;

        logger.debug("Calculated battle power for {}: {}", pokemon.getName(), battlePower);
        return battlePower;
    }

    @Override
    @Transactional(readOnly = true)
    public String comparePokemon(Long pokemon1Id, Long pokemon2Id) {
        Pokemon pokemon1 = pokemonRepository.findById(pokemon1Id)
                .orElseThrow(() -> new RuntimeException("Pokemon not found with ID: " + pokemon1Id));
        Pokemon pokemon2 = pokemonRepository.findById(pokemon2Id)
                .orElseThrow(() -> new RuntimeException("Pokemon not found with ID: " + pokemon2Id));

        Double power1 = calculateBattlePower(pokemon1Id);
        Double power2 = calculateBattlePower(pokemon2Id);

        StringBuilder comparison = new StringBuilder();
        comparison.append(String.format("%s (Power: %.2f) vs %s (Power: %.2f)\n",
                pokemon1.getName(), power1, pokemon2.getName(), power2));

        if (power1 > power2) {
            comparison.append(String.format("%s is stronger!", pokemon1.getName()));
        } else if (power2 > power1) {
            comparison.append(String.format("%s is stronger!", pokemon2.getName()));
        } else {
            comparison.append("Both Pokemon have equal battle power!");
        }

        return comparison.toString();
    }

    // Statistics Operations
    @Override
    @Transactional(readOnly = true)
    public Long getTotalPokemonCount() {
        return pokemonRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountByGeneration(Integer generation) {
        return pokemonRepository.countByGeneration(generation);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountByType(PokemonType type) {
        return pokemonRepository.countByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageTotalStats() {
        return pokemonRepository.getAverageTotalStats();
    }

    // Validation Operations
    @Override
    public void validatePokemonData(Pokemon pokemon) {
        logger.debug("Validating Pokemon data: {}", pokemon.getName());

        if (pokemon.getName() == null || pokemon.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Pokemon name is required");
        }

        if (pokemon.getPrimaryType() == null) {
            throw new IllegalArgumentException("Primary type is required");
        }

        if (pokemon.getPokedexNumber() != null && pokemon.getPokedexNumber() <= 0) {
            throw new IllegalArgumentException("Pokedex number must be positive");
        }

        if (pokemon.getStats() == null) {
            throw new IllegalArgumentException("Pokemon stats are required");
        }

        // Validate type exists
        if (!pokemonTypeService.existsById(pokemon.getPrimaryType().getId())) {
            throw new IllegalArgumentException("Primary type does not exist");
        }

        if (pokemon.getSecondaryType() != null &&
                !pokemonTypeService.existsById(pokemon.getSecondaryType().getId())) {
            throw new IllegalArgumentException("Secondary type does not exist");
        }
    }

    @Override
    public void validateUniqueConstraints(Pokemon pokemon, Long excludeId) {
        logger.debug("Validating unique constraints for Pokemon: {}", pokemon.getName());

        // Check name uniqueness
        Optional<Pokemon> existingByName = pokemonRepository.findByNameIgnoreCase(pokemon.getName());
        if (existingByName.isPresent() &&
                (excludeId == null || !existingByName.get().getId().equals(excludeId))) {
            throw new IllegalArgumentException("Pokemon name already exists: " + pokemon.getName());
        }

        // Check Pokedex number uniqueness
        if (pokemon.getPokedexNumber() != null) {
            Optional<Pokemon> existingByPokedex = pokemonRepository.findByPokedexNumber(pokemon.getPokedexNumber());
            if (existingByPokedex.isPresent() &&
                    (excludeId == null || !existingByPokedex.get().getId().equals(excludeId))) {
                throw new IllegalArgumentException("Pokedex number already exists: " + pokemon.getPokedexNumber());
            }
        }
    }
}