package com.example.examen_db.repository.db;

import com.example.examen_db.domain.Entity;
import com.example.examen_db.repository.memory.InMemoryRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected Connection connection;

    public AbstractDBRepository(Connection connection) {
        this.connection = connection;
    }

    public abstract E resultSetToEntity(ResultSet resultSet) throws SQLException;
    // Metoda abstracta pentru insert
    public abstract void save(E entity);

    // Metoda pentru incarcarea datelor din BD in memorie
    protected abstract void load();

    //Metoda pentru a adauga entitatea in memorie
    public void add(E entity) {
        super.save(entity);
    }
}
