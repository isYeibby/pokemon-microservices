package com.pokemon.microservices.pokemon.service.impl;

import com.pokemon.microservices.pokemon.entity.Pokemon;
import com.pokemon.microservices.pokemon.entity.PokemonType;
import com.pokemon.microservices.pokemon.repository.PokemonRepository;
import com.pokemon.microservices.pokemon.service.PokemonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final PokemonRepository pokemonRepository;

    @Override
    public Pokemon createPokemon(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    @Override
    public Pokemon updatePokemon(Long id, Pokemon pokemon) {
        return pokemonRepository.findById(id)
                .map(existing -> {
                    existing.setName(pokemon.getName());
                    existing.setPokedexNumber(pokemon.getPokedexNumber());
                    existing.setDescription(pokemon.getDescription());
                    existing.setHeight(pokemon.getHeight());
                    existing.setWeight(pokemon.getWeight());
                    existing.setPrimaryType(pokemon.getPrimaryType());
                    existing.setSecondaryType(pokemon.getSecondaryType());
                    existing.setHp(pokemon.getHp());
                    existing.setAttack(pokemon.getAttack());
                    existing.setDefense(pokemon.getDefense());
                    existing.setSpecialAttack(pokemon.getSpecialAttack());
                    existing.setSpecialDefense(pokemon.getSpecialDefense());
                    existing.setSpeed(pokemon.getSpeed());
                    existing.setIsLegendary(pokemon.getIsLegendary());
                    existing.setIsMythical(pokemon.getIsMythical());
                    existing.setGeneration(pokemon.getGeneration());
                    existing.setImageUrl(pokemon.getImageUrl());
                    return pokemonRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Pokemon not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pokemon> findById(Long id) {
        return pokemonRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pokemon> findByPokedexNumber(Integer pokedexNumber) {
        return pokemonRepository.findByPokedexNumber(pokedexNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pokemon> findByName(String name) {
        return pokemonRepository.findByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pokemon> findAll(Pageable pageable) {
        return pokemonRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        pokemonRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return pokemonRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return pokemonRepository.findByNameIgnoreCase(name).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPokedexNumber(Integer pokedexNumber) {
        return pokemonRepository.findByPokedexNumber(pokedexNumber).isPresent();
    }

    // Implementaciones mínimas para los demás métodos
    @Override
    public Page<Pokemon> searchPokemon(String searchTerm, Pageable pageable) {
        return pokemonRepository.findAll(pageable); // Implementación básica
    }

    @Override
    public List<Pokemon> findByType(PokemonType type) {
        return pokemonRepository.findByPrimaryType(type);
    }

    @Override
    public List<Pokemon> findByGeneration(Integer generation) {
        return pokemonRepository.findByGeneration(generation);
    }

    @Override
    public List<Pokemon> findLegendaryPokemon() {
        return pokemonRepository.findByIsLegendaryTrue();
    }

    @Override
    public List<Pokemon> findMythicalPokemon() {
        return pokemonRepository.findByIsMythicalTrue();
    }

    @Override
    public Page<Pokemon> findWithFilters(Integer generation, PokemonType primaryType,
                                         Boolean isLegendary, Integer minPokedexNumber,
                                         Integer maxPokedexNumber, Pageable pageable) {
        return pokemonRepository.findAll(pageable);
    }

    // Métodos básicos para evolución
    @Override
    public List<Pokemon> getEvolutionChain(Long pokemonId) {
        return List.of();
    }

    @Override
    public Pokemon evolvePokemon(Long pokemonId) {
        return findById(pokemonId).orElse(null);
    }

    @Override
    public boolean canEvolve(Long pokemonId) {
        return false;
    }

    @Override
    public List<Pokemon> getBaseForms() {
        return pokemonRepository.findAll();
    }

    @Override
    public List<Pokemon> getFinalForms() {
        return pokemonRepository.findAll();
    }

    // Métodos básicos para stats
    @Override
    public List<Pokemon> findByMinTotalStats(Integer minTotal) {
        return pokemonRepository.findAll();
    }

    @Override
    public List<Pokemon> getFastestPokemon(Integer minSpeed) {
        return pokemonRepository.findAll();
    }

    @Override
    public List<Pokemon> getStrongestPokemon(Integer minAttack) {
        return pokemonRepository.findAll();
    }

    @Override
    public Double calculateBattlePower(Long pokemonId) {
        return 0.0;
    }

    @Override
    public String comparePokemon(Long pokemon1Id, Long pokemon2Id) {
        return "Comparison not implemented";
    }

    @Override
    public Long getTotalPokemonCount() {
        return pokemonRepository.count();
    }

    @Override
    public Long getCountByGeneration(Integer generation) {
        return 0L;
    }

    @Override
    public Long getCountByType(PokemonType type) {
        return 0L;
    }

    @Override
    public Double getAverageTotalStats() {
        return 0.0;
    }

    @Override
    public void validatePokemonData(Pokemon pokemon) {
        // Validación básica
    }

    @Override
    public void validateUniqueConstraints(Pokemon pokemon, Long excludeId) {
        // Validación básica
    }
}