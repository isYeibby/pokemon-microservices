package com.pokemon.microservices.pokemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PokemonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokemonServiceApplication.class, args);
        System.out.println("""
            
            🐾 Pokemon Service Started Successfully! 🐾
            
            🌐 Service URL: http://localhost:8081
            📊 Health Check: http://localhost:8081/actuator/health
            🔗 API Docs: http://localhost:8081/api/pokemon
            
            🎯 Available Endpoints:
            📋 GET    /api/pokemon          - List all Pokemon
            👁️  GET    /api/pokemon/{id}     - Get Pokemon by ID
            ➕ POST   /api/pokemon          - Create new Pokemon
            ✏️  PUT    /api/pokemon/{id}     - Update Pokemon
            🗑️  DELETE /api/pokemon/{id}     - Delete Pokemon
            🎨 GET    /api/pokemon/types    - List Pokemon types
            
            🔗 Connected to:
            📋 Config Server: http://localhost:8888
            🌐 Eureka Server: http://localhost:8761
            🗄️  PostgreSQL: pokemon_db
            
            """);
    }
}