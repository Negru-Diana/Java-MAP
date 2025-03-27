package com.example.retea_de_socializare.repository.memory;

import com.example.retea_de_socializare.domain.Entity;
import com.example.retea_de_socializare.domain.Prietenie;
import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.exceptions.ValidationException;
import com.example.retea_de_socializare.domain.validators.Validator;
import com.example.retea_de_socializare.repository.Repository;

import java.util.*;

public class InMemoryRepository<ID, E extends Entity<ID>> implements  Repository<ID, E> {

    private Validator<E> validator;
    protected Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        //Java 8 features
        //In loc de if(id==null)
        Objects.requireNonNull(id, "id must not be null.");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) throws ValidationException {
        //Java 8 features
        //In loc de if(entity==null)
        Objects.requireNonNull(entity, "entity must not be null.");
        validator.validate(entity);
        if(entity instanceof Utilizator){
            //Java 8 features
            //Daca nu exista entity se adauga si returneaza null, altfel se returneaza entitatea existenta
            return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
        }
        else if(entity instanceof Prietenie){
            Prietenie prietenie = (Prietenie) entity;
            Utilizator u1 = prietenie.getUtilizator1();
            Utilizator u2 = prietenie.getUtilizator2();

            ArrayList<Utilizator> friends1 = u1.getFriends();
            ArrayList<Utilizator> friends2 = u2.getFriends();

            if(friends1.contains(u2) == true || friends2.contains(u1) == true){
                //Java 8 features
                //Returneaza id-ul lui entity, altfel returneaza null
                return Optional.ofNullable(entities.get(entity.getId()));
            }
            else{
                friends1.add(u2);
                friends2.add(u1);
                entities.put(entity.getId(), entity);
                return Optional.empty();
            }
        }
        //Returneaza null
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        //Java 8 features
        //In loc de if(id==null)
        Objects.requireNonNull(id, "id must not be null.");

        //Sterge entitatea cu id-ul dat, altfel returneaza null
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        //Java 8 features
        //In loc de if(entity==null)
        Objects.requireNonNull(entity, "entity must not be null.");
        validator.validate(entity);

        //face update daca entitatea nu este null si returneaza null, altfelreturneaza entitatea
        return Optional.ofNullable(entities.replace(entity.getId(), entity));
    }
}
