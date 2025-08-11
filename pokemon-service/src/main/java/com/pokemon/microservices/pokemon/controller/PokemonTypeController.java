package com.pokemon.microservices.pokemon.controller;

import com.pokemon.microservices.pokemon.entity.PokemonType;
import com.pokemon.microservices.pokemon.service.PokemonTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Pokemon Type REST Controller
 *
 * Provides REST endpoints for Pokemon Type management operations.
 *
 * @author isYeibby
 */
@RestController
@RequestMapping("/api/pokemon/types")
@CrossOrigin(origins = "*")
public class PokemonTypeController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonTypeController.class);

    private final PokemonTypeService pokemonTypeService;

    @Autowired
    public PokemonTypeController(PokemonTypeService pokemonTypeService) {
        this.pokemonTypeService = pokemonTypeService;
    }

    // CRUD Endpoints

    @PostMapping
    public ResponseEntity<PokemonType> createType(@Valid @RequestBody PokemonType type) {
        logger.info("REST: Creating new Pokemon type: {}", type.getName());

        PokemonType createdType = pokemonTypeService.createType(type);
        return new ResponseEntity<>(createdType, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PokemonType>> getAllTypes() {
        logger.debug("REST: Getting all Pokemon types");

        List<PokemonType> types = pokemonTypeService.findAll();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PokemonType> getTypeById(@PathVariable Long id) {
        logger.debug("REST: Getting Pokemon type by ID: {}", id);

        Optional<PokemonType> type = pokemonTypeService.findById(id);
        return type.map(t -> ResponseEntity.ok(t))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PokemonType> getTypeByName(@PathVariable String name) {
        logger.debug("REST: Getting Pokemon type by name: {}", name);

        Optional<PokemonType> type = pokemonTypeService.findByName(name);
        return type.map(t -> ResponseEntity.ok(t))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PokemonType> updateType(@PathVariable Long id, @Valid @RequestBody PokemonType type) {
        logger.info("REST: Updating Pokemon type with ID: {}", id);

        PokemonType updatedType = pokemonTypeService.updateType(id, type);
        return ResponseEntity.ok(updatedType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        logger.info("REST: Deleting Pokemon type with ID: {}", id);

        pokemonTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Search Endpoints

    @GetMapping("/search")
    public ResponseEntity<List<PokemonType>> searchTypes(@RequestParam String term) {
        logger.debug("REST: Searching Pokemon types with term: {}", term);

        List<PokemonType> types = pokemonTypeService.searchTypes(term);
        return ResponseEntity.ok(types);
    }

    // Statistics Endpoints

    @GetMapping("/stats/usage")
    public ResponseEntity<List<Object[]>> getTypeUsageStats() {
        logger.debug("REST: Getting type usage statistics");

        List<Object[]> stats = pokemonTypeService.getTypesWithPokemonCount();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}/count")
    public ResponseEntity<Long> getCountByType(@PathVariable Long id) {
        logger.debug("REST: Getting Pokemon count for type ID: {}", id);

        Optional<PokemonType> type = pokemonTypeService.findById(id);
        if (type.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Long count = pokemonTypeService.getCountByType(type.get());
        return ResponseEntity.ok(count);
    }

    // Health Check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Pokemon Type Service is running successfully!");
    }
}