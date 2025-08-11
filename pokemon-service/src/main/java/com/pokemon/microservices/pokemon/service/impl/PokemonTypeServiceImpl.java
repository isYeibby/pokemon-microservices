package com.pokemon.microservices.pokemon.service.impl;

import com.pokemon.microservices.pokemon.entity.PokemonType;
import com.pokemon.microservices.pokemon.repository.PokemonTypeRepository;
import com.pokemon.microservices.pokemon.service.PokemonTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PokemonTypeServiceImpl implements PokemonTypeService {

    private static final Logger logger = LoggerFactory.getLogger(PokemonTypeServiceImpl.class);

    private final PokemonTypeRepository pokemonTypeRepository;

    @Autowired
    public PokemonTypeServiceImpl(PokemonTypeRepository pokemonTypeRepository) {
        this.pokemonTypeRepository = pokemonTypeRepository;
    }

    @Override
    public PokemonType createType(PokemonType type) {
        logger.info("Creating new Pokemon type: {}", type.getName());

        if (existsByName(type.getName())) {
            throw new IllegalArgumentException("Pokemon type already exists: " + type.getName());
        }

        PokemonType savedType = pokemonTypeRepository.save(type);
        logger.info("Pokemon type created successfully: {}", savedType.getName());

        return savedType;
    }

    @Override
    public PokemonType updateType(Long id, PokemonType type) {
        logger.info("Updating Pokemon type with ID: {}", id);

        PokemonType existingType = pokemonTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pokemon type not found with ID: " + id));

        // Check name uniqueness (excluding current record)
        Optional<PokemonType> existingByName = pokemonTypeRepository.findByNameIgnoreCase(type.getName());
        if (existingByName.isPresent() && !existingByName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Pokemon type name already exists: " + type.getName());
        }

        existingType.setName(type.getName());
        existingType.setColor(type.getColor());
        existingType.setDescription(type.getDescription());

        PokemonType updatedType = pokemonTypeRepository.save(existingType);
        logger.info("Pokemon type updated successfully: {}", updatedType.getName());

        return updatedType;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PokemonType> findById(Long id) {
        return pokemonTypeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PokemonType> findByName(String name) {
        return pokemonTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PokemonType> findAll() {
        return pokemonTypeRepository.findAllByOrderByNameAsc();
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting Pokemon type with ID: {}", id);

        if (!pokemonTypeRepository.existsById(id)) {
            throw new RuntimeException("Pokemon type not found with ID: " + id);
        }

        pokemonTypeRepository.deleteById(id);
        logger.info("Pokemon type deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return pokemonTypeRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return pokemonTypeRepository.existsByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PokemonType> searchTypes(String searchTerm) {
        return pokemonTypeRepository.searchByNameOrDescription(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountByType(PokemonType type) {
        return pokemonTypeRepository.countPokemonByType(type.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getTypesWithPokemonCount() {
        return pokemonTypeRepository.findTypesWithPokemonCount();
    }
}