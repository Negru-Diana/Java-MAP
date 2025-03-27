package com.example.retea_de_socializare.repository;

import com.example.retea_de_socializare.domain.Entity;
import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;

/**
 * CRUD operations com.example.retea_de_socializare.repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> - type of entities saved in com.example.retea_de_socializare.repository
 */

public interface Repository<ID, E extends Entity<ID>> {

    /**
     *
     * @param id - the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException - if id is null.
     *
     */
    Optional<E> findOne(ID id);

    /**
     *
     * @return all entities
     *
     */
    Iterable<E> findAll();

    /**
     *
     * @param entity - must not be null
     * @return null - if the given entity is saved
     *              otherwise returns the entity (id already exists)
     * @throws IllegalArgumentException - if the given entity is null.
     *
     */
    Optional<E> save(E entity);

    /**
     * removes the entity with the specified id
     * @param id - must be not null
     * @return the removed entity - if the entity with the given id was removed
     *         null - it there is no entity with the given id
     * @throws  IllegalArgumentException - if the given id is null.
     *
     */
    Optional<E> delete(ID id);

    /**
     *
     * @param entity - must be not null
     * @return null - if the entity is updated,
     *         otherwise returns the entity - (e.g. id does not exist).
     * @throws  IllegalArgumentException - if the given entity is null.
     * @throws ValidationException - if the entity is not valid.
     *
     */
    Optional<E> update(E entity);
}

