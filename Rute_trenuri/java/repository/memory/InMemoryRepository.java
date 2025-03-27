package com.example.examen_db.repository.memory;

import com.example.examen_db.domain.Entity;
import com.example.examen_db.repository.IRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements IRepository<ID, E> {
    protected Map<ID, E> entities;

    public InMemoryRepository() {
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        Objects.requireNonNull(id, "id must not be null.");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public void save(E entity) {
        entities.put(entity.getId(), entity);
    }

}
