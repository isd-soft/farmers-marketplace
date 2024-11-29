package com.example.isdfarmersmarket.dao.enums;

public enum UnitType {
    KILOGRAM("kg"),
    LITER("l"),
    PIECE("pc");

    private final String shortName;
    UnitType(String shortName) {
        this.shortName = shortName;
    }
    public String getShortName() {
        return shortName;
    }
}
