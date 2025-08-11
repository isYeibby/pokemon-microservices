package com.pokemon.microservices.pokemon.controller;

import com.pokemon.microservices.pokemon.entity.Pokemon;
import com.pokemon.microservices.pokemon.entity.PokemonType;
import com.pokemon.microservices.pokemon.service.PokemonService;
import com.pokemon.microservices.pokemon.service.PokemonTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Pokemon REST Controller
 *
 * Provides REST endpoints for Pokemon management operations including
 * CRUD, search, evolution, and battle functionality.
 *
 * @author isYeibby
 */
@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "*")
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    private final PokemonService pokemonService;
    private final PokemonTypeService pokemonTypeService;

    @Autowired
    public PokemonController(PokemonService pokemonService, PokemonTypeService pokemonTypeService) {
        this.pokemonService = pokemonService;
        this.pokemonTypeService = pokemonTypeService;
    }

    // CRUD Endpoints

    @PostMapping
    public ResponseEntity<Pokemon> createPokemon(@Valid @RequestBody Pokemon pokemon) {
        logger.info("REST: Creating new Pokemon: {}", pokemon.getName());

        Pokemon createdPokemon = pokemonService.createPokemon(pokemon);
        return new ResponseEntity<>(createdPokemon, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Pokemon>> getAllPokemon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "pokedexNumber") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        logger.debug("REST: Getting all Pokemon - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Pokemon> pokemonPage = pokemonService.findAll(pageable);
        return ResponseEntity.ok(pokemonPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> getPokemonById(@PathVariable Long id) {
        logger.debug("REST: Getting Pokemon by ID: {}", id);

        Optional<Pokemon> pokemon = pokemonService.findById(id);
        return pokemon.map(p -> ResponseEntity.ok(p))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pokedex/{number}")
    public ResponseEntity<Pokemon> getPokemonByPokedexNumber(@PathVariable Integer number) {
        logger.debug("REST: Getting Pokemon by Pokedex number: {}", number);

        Optional<Pokemon> pokemon = pokemonService.findByPokedexNumber(number);
        return pokemon.map(p -> ResponseEntity.ok(p))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Pokemon> getPokemonByName(@PathVariable String name) {
        logger.debug("REST: Getting Pokemon by name: {}", name);

        Optional<Pokemon> pokemon = pokemonService.findByName(name);
        return pokemon.map(p -> ResponseEntity.ok(p))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pokemon> updatePokemon(@PathVariable Long id, @Valid @RequestBody Pokemon pokemon) {
        logger.info("REST: Updating Pokemon with ID: {}", id);

        Pokemon updatedPokemon = pokemonService.updatePokemon(id, pokemon);
        return ResponseEntity.ok(updatedPokemon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokemon(@PathVariable Long id) {
        logger.info("REST: Deleting Pokemon with ID: {}", id);

        pokemonService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Search Endpoints

    @GetMapping("/search")
    public ResponseEntity<Page<Pokemon>> searchPokemon(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        logger.debug("REST: Searching Pokemon with term: {}", term);

        Pageable pageable = PageRequest.of(page, size);
        Page<Pokemon> results = pokemonService.searchPokemon(term, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Pokemon>> filterPokemon(
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) Boolean legendary,
            @RequestParam(required = false) Integer minPokedex,
            @RequestParam(required = false) Integer maxPokedex,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        logger.debug("REST: Filtering Pokemon with multiple criteria");

        PokemonType primaryType = null;
        if (typeId != null) {
            Optional<PokemonType> type = pokemonTypeService.findById(typeId);
            if (type.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            primaryType = type.get();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Pokemon> results = pokemonService.findWithFilters(
                generation, primaryType, legendary, minPokedex, maxPokedex, pageable);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<Pokemon>> getPokemonByType(@PathVariable Long typeId) {
        logger.debug("REST: Getting Pokemon by type ID: {}", typeId);

        Optional<PokemonType> type = pokemonTypeService.findById(typeId);
        if (type.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Pokemon> pokemon = pokemonService.findByType(type.get());
        return ResponseEntity.ok(pokemon);
    }

    @GetMapping("/generation/{generation}")
    public ResponseEntity<List<Pokemon>> getPokemonByGeneration(@PathVariable Integer generation) {
        logger.debug("REST: Getting Pokemon by generation: {}", generation);

        List<Pokemon> pokemon = pokemonService.findByGeneration(generation);
        return ResponseEntity.ok(pokemon);
    }

    @GetMapping("/legendary")
    public ResponseEntity<List<Pokemon>> getLegendaryPokemon() {
        logger.debug("REST: Getting all legendary Pokemon");

        List<Pokemon> legendary = pokemonService.findLegendaryPokemon();
        return ResponseEntity.ok(legendary);
    }

    @GetMapping("/mythical")
    public ResponseEntity<List<Pokemon>> getMythicalPokemon() {
        logger.debug("REST: Getting all mythical Pokemon");

        List<Pokemon> mythical = pokemonService.findMythicalPokemon();
        return ResponseEntity.ok(mythical);
    }

    // Evolution Endpoints

    @GetMapping("/{id}/evolution-chain")
    public ResponseEntity<List<Pokemon>> getEvolutionChain(@PathVariable Long id) {
        logger.debug("REST: Getting evolution chain for Pokemon ID: {}", id);

        List<Pokemon> evolutionChain = pokemonService.getEvolutionChain(id);
        return ResponseEntity.ok(evolutionChain);
    }

    @PostMapping("/{id}/evolve")
    public ResponseEntity<Pokemon> evolvePokemon(@PathVariable Long id) {
        logger.info("REST: Evolving Pokemon with ID: {}", id);

        Pokemon evolution = pokemonService.evolvePokemon(id);
        return ResponseEntity.ok(evolution);
    }

    @GetMapping("/{id}/can-evolve")
    public ResponseEntity<Boolean> canPokemonEvolve(@PathVariable Long id) {
        logger.debug("REST: Checking if Pokemon can evolve: {}", id);

        boolean canEvolve = pokemonService.canEvolve(id);
        return ResponseEntity.ok(canEvolve);
    }

    // Battle and Stats Endpoints

    @GetMapping("/stats/strongest")
    public ResponseEntity<List<Pokemon>> getStrongestPokemon(
            @RequestParam(defaultValue = "100") Integer minAttack) {

        logger.debug("REST: Getting strongest Pokemon with min attack: {}", minAttack);

        List<Pokemon> strongest = pokemonService.getStrongestPokemon(minAttack);
        return ResponseEntity.ok(strongest);
    }

    @GetMapping("/stats/fastest")
    public ResponseEntity<List<Pokemon>> getFastestPokemon(
            @RequestParam(defaultValue = "100") Integer minSpeed) {

        logger.debug("REST: Getting fastest Pokemon with min speed: {}", minSpeed);

        List<Pokemon> fastest = pokemonService.getFastestPokemon(minSpeed);
        return ResponseEntity.ok(fastest);
    }

    @GetMapping("/stats/min-total/{minTotal}")
    public ResponseEntity<List<Pokemon>> getPokemonByMinStats(@PathVariable Integer minTotal) {
        logger.debug("REST: Getting Pokemon with min total stats: {}", minTotal);

        List<Pokemon> pokemon = pokemonService.findByMinTotalStats(minTotal);
        return ResponseEntity.ok(pokemon);
    }

    @GetMapping("/{id}/battle-power")
    public ResponseEntity<Double> getBattlePower(@PathVariable Long id) {
        logger.debug("REST: Calculating battle power for Pokemon ID: {}", id);

        Double battlePower = pokemonService.calculateBattlePower(id);
        return ResponseEntity.ok(battlePower);
    }

    @GetMapping("/compare/{id1}/vs/{id2}")
    public ResponseEntity<String> comparePokemon(@PathVariable Long id1, @PathVariable Long id2) {
        logger.debug("REST: Comparing Pokemon {} vs {}", id1, id2);

        String comparison = pokemonService.comparePokemon(id1, id2);
        return ResponseEntity.ok(comparison);
    }

    // Statistics Endpoints

    @GetMapping("/stats/count")
    public ResponseEntity<Long> getTotalCount() {
        logger.debug("REST: Getting total Pokemon count");

        Long count = pokemonService.getTotalPokemonCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/average-stats")
    public ResponseEntity<Double> getAverageStats() {
        logger.debug("REST: Getting average total stats");

        Double average = pokemonService.getAverageTotalStats();
        return ResponseEntity.ok(average);
    }

    @GetMapping("/stats/generation/{generation}/count")
    public ResponseEntity<Long> getCountByGeneration(@PathVariable Integer generation) {
        logger.debug("REST: Getting Pokemon count for generation: {}", generation);

        Long count = pokemonService.getCountByGeneration(generation);
        return ResponseEntity.ok(count);
    }

    // Health Check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Pokemon Service is running successfully!");
    }
}