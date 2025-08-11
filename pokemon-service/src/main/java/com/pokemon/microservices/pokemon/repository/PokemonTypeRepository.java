package com.pokemon.microservices.pokemon.repository;

import com.pokemon.microservices.pokemon.entity.PokemonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonTypeRepository extends JpaRepository<PokemonType, Long> {

    // Basic finders
    Optional<PokemonType> findByNameIgnoreCase(String name);

    // Existence checks
    boolean existsByNameIgnoreCase(String name);

    // Ordered queries
    List<PokemonType> findAllByOrderByNameAsc();
    List<PokemonType> findAllByOrderByIdAsc();

    // Search functionality
    @Query("SELECT pt FROM PokemonType pt WHERE " +
            "LOWER(pt.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pt.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PokemonType> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

    // Statistics
    @Query("SELECT pt, COUNT(p) FROM PokemonType pt " +
            "LEFT JOIN pt.pokemonPrimaryType p " +
            "GROUP BY pt " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> findTypesWithPokemonCount();

    @Query("SELECT COUNT(p) FROM Pokemon p WHERE p.primaryType.id = :typeId OR p.secondaryType.id = :typeId")
    Long countPokemonByType(@Param("typeId") Long typeId);
}