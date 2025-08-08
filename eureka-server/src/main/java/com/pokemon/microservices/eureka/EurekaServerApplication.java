package com.pokemon.microservices.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        System.out.println("""
            
            🌐 Pokemon Eureka Server Started Successfully! 🌐
            
            📊 Eureka Dashboard: http://localhost:8761
            🔍 Service Registry: http://localhost:8761/eureka/apps
            💚 Health Check: http://localhost:8761/actuator/health
            
            🎯 Ready for Pokemon microservices registration!
            🔗 Config Server: http://localhost:8888
            
            """);
    }
}