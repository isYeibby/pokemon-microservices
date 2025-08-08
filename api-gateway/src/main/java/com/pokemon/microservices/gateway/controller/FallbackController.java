package com.pokemon.microservices.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fallback Controller
 *
 * Provides fallback responses when microservices are unavailable.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/pokemon")
    public ResponseEntity<String> pokemonFallback() {
        return ResponseEntity.ok("""
            {
                "message": "Pokemon Service is temporarily unavailable",
                "status": "fallback",
                "timestamp": "%s"
            }
            """.formatted(java.time.Instant.now()));
    }

    @GetMapping("/trainers")
    public ResponseEntity<String> trainerFallback() {
        return ResponseEntity.ok("""
            {
                "message": "Trainer Service is temporarily unavailable",
                "status": "fallback", 
                "timestamp": "%s"
            }
            """.formatted(java.time.Instant.now()));
    }

    @GetMapping("/teams")
    public ResponseEntity<String> teamFallback() {
        return ResponseEntity.ok("""
            {
                "message": "Team Service is temporarily unavailable",
                "status": "fallback",
                "timestamp": "%s"
            }
            """.formatted(java.time.Instant.now()));
    }
}