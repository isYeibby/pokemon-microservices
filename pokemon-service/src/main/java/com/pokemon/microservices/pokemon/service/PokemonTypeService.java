package com.pokemon.microservices.pokemon.service;

import com.pokemon.microservices.pokemon.entity.PokemonType;

import java.util.List;
import java.util.Optional;

public interface PokemonTypeService {

    // CRUD Operations
    PokemonType createType(PokemonType type);
    PokemonType updateType(Long id, PokemonType type);
    Optional<PokemonType> findById(Long id);
    Optional<PokemonType> findByName(String name);
    List<PokemonType> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByName(String name);

    // Search Operations
    List<PokemonType> searchTypes(String searchTerm);

    // Statistics
    Long getCountByType(PokemonType type);
    List<Object[]> getTypesWithPokemonCount();
}