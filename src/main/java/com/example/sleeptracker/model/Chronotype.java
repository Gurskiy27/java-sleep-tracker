package com.example.sleeptracker.model;

public enum Chronotype {
    LARK("жаворонок"),
    PIGEON("голубь"),
    OWL("сова");

    private final String russianName;

    Chronotype(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }

    @Override
    public String toString() {
        return russianName;
    }
}