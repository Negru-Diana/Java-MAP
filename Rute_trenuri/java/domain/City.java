package com.example.examen_db.domain;

public class City extends Entity<String>{
    private String name;

    public City(String id, String name) {
        super.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
