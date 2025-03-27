package com.example.examen_db.repository;

import com.example.examen_db.domain.Entity;

import java.util.Optional;

public interface IRepository<ID, E extends Entity<ID>> {
    Optional<E> findOne(ID id);
    Iterable<E> findAll();
    void save(E entity);
    //Optional<E> delete(ID id);
    //Optional<E> update(E entity);
}
