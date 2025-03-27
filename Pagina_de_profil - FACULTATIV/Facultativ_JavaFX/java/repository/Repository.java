package com.example.facultativ_javafx.repository;

import java.util.List;

public interface Repository<T> {
    // Metoda pentru a salva un utilizator
    void save(T entity);

    // Metoda pentru a gasi toti utilizatorii
    List<T> findAll();

    //Metoda pentru a gasi un utilizator dupa username
    T findByUsername(String username);

    //Metoda pentru update
    void update(T entity, T newEntity);
}
