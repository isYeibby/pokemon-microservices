package com.pokemon.microservices.pokemon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Embeddable
public class PokemonStats {

    @Column(name = "hp")
    @Min(value = 1, message = "HP must be at least 1")
    @Max(value = 255, message = "HP must not exceed 255")
    private Integer hp = 50; // Hit Points

    @Column(name = "attack")
    @Min(value = 1, message = "Attack must be at least 1")
    @Max(value = 255, message = "Attack must not exceed 255")
    private Integer attack = 50;

    @Column(name = "defense")
    @Min(value = 1, message = "Defense must be at least 1")
    @Max(value = 255, message = "Defense must not exceed 255")
    private Integer defense = 50;

    @Column(name = "special_attack")
    @Min(value = 1, message = "Special Attack must be at least 1")
    @Max(value = 255, message = "Special Attack must not exceed 255")
    private Integer specialAttack = 50;

    @Column(name = "special_defense")
    @Min(value = 1, message = "Special Defense must be at least 1")
    @Max(value = 255, message = "Special Defense must not exceed 255")
    private Integer specialDefense = 50;

    @Column(name = "speed")
    @Min(value = 1, message = "Speed must be at least 1")
    @Max(value = 255, message = "Speed must not exceed 255")
    private Integer speed = 50;

    // Constructors
    public PokemonStats() {
    }

    public PokemonStats(Integer hp, Integer attack, Integer defense,
                        Integer specialAttack, Integer specialDefense, Integer speed) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
    }

    // Business methods
    public int getTotal() {
        return hp + attack + defense + specialAttack + specialDefense + speed;
    }

    public double getAverage() {
        return getTotal() / 6.0;
    }

    public int getPhysicalOffense() {
        return attack;
    }

    public int getPhysicalDefense() {
        return defense;
    }

    public int getSpecialOffense() {
        return specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public boolean isBalanced() {
        int total = getTotal();
        int avg = total / 6;
        int tolerance = 20; // 20 points tolerance

        return Math.abs(hp - avg) <= tolerance &&
                Math.abs(attack - avg) <= tolerance &&
                Math.abs(defense - avg) <= tolerance &&
                Math.abs(specialAttack - avg) <= tolerance &&
                Math.abs(specialDefense - avg) <= tolerance &&
                Math.abs(speed - avg) <= tolerance;
    }

    public String getDominantStat() {
        int max = Math.max(Math.max(Math.max(hp, attack), Math.max(defense, specialAttack)),
                Math.max(specialDefense, speed));

        if (hp == max) return "HP";
        if (attack == max) return "Attack";
        if (defense == max) return "Defense";
        if (specialAttack == max) return "Special Attack";
        if (specialDefense == max) return "Special Defense";
        if (speed == max) return "Speed";

        return "Balanced";
    }

    public String getWeakestStat() {
        int min = Math.min(Math.min(Math.min(hp, attack), Math.min(defense, specialAttack)),
                Math.min(specialDefense, speed));

        if (hp == min) return "HP";
        if (attack == min) return "Attack";
        if (defense == min) return "Defense";
        if (specialAttack == min) return "Special Attack";
        if (specialDefense == min) return "Special Defense";
        if (speed == min) return "Speed";

        return "Balanced";
    }

    // Getters and Setters
    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(Integer specialAttack) {
        this.specialAttack = specialAttack;
    }

    public Integer setSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(Integer specialDefense) {
        this.specialDefense = specialDefense;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "PokemonStats{" +
                "hp=" + hp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", specialAttack=" + specialAttack +
                ", specialDefense=" + specialDefense +
                ", speed=" + speed +
                ", total=" + getTotal() +
                '}';
    }
}