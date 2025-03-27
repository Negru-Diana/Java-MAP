package com.example.retea_de_socializare.repository.database;

import com.example.retea_de_socializare.domain.CererePrietenie;
import com.example.retea_de_socializare.domain.Entity;
import com.example.retea_de_socializare.domain.Grup;
import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.domain.validators.Validator;
import com.example.retea_de_socializare.exceptions.RepoException;
import com.example.retea_de_socializare.exceptions.ValidationException;
import com.example.retea_de_socializare.repository.Repository;
import com.example.retea_de_socializare.repository.memory.InMemoryRepository;

import java.sql.*;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    protected Connection connection;
    // Repository-ul pentru Utilizator
    protected Repository<Long, Utilizator> utilizatorRepository;

    public AbstractDBRepository(Validator<E> validator, Connection connection) {
        super(validator);
        this.connection = connection;
    }

    // Metoda abstracta pentru conversia unui rezultat SQL intr-o entitate (Entity)
    public abstract E resultSetToEntity(ResultSet resultSet) throws SQLException;

    // Metoda abstracta conversie entitate --> SQL pentru insert
    public abstract String entityToInsertSQL(E entity);

    // Metoda abstracta conversie entitate --> SQL pentru update
    public abstract String entityToUpdateSQL(E entity);

    // Metoda abstracta pentru setarea parametrilor intr-un PreparedStatement pentru inserare
    protected abstract void setInsertParameters(PreparedStatement preparedStatement, E entity) throws SQLException;

    // Metoda abstracta pentru setarea parametrilor intr-un PreparedStatement pentru update
    protected abstract void setUpdateParameters(PreparedStatement preparedStatement, E entity) throws SQLException;

    // Metoda abstracta pentru a obtine numele tabelei
    protected  abstract String getTableName();

    // Metoda pentru incarcarea datelor din BD in memorie
    protected abstract void load();


    // Metoda pentru a salva o entitate in baza de date
    @Override
    public Optional<E> save(E entity) throws ValidationException {
        Optional<E> savedEntity = super.save(entity);

        // Daca entitatea trebuie salvata in baza de date
        if(savedEntity.isEmpty()){
            if(entity instanceof Grup){
                try{
                    addMembersToGroup((Long) entity.getId(), ((Grup) entity).getMembri());
                }
                catch (Exception e){
                    throw new RepoException("A aparut o problema la salvarea datelor despre grup");
                }
            }

            // Executa INSERT in baza de date
            try (PreparedStatement statement = connection.prepareStatement(entityToInsertSQL(entity))) {
                // Setez parametrii pentru INSERT
                setInsertParameters(statement, entity);

                // Execut INSERT
                int affectedRows = statement.executeUpdate();

                // Daca nu au fost afectate randuri ==> entitatea nu a fost salvata
                if(affectedRows == 0){
                    throw new RepoException("Nu s-au produs modificari la BD in urma salvarii! \n");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw  new RepoException("A aparut o eroare la salvare in baza de date! \n");
            }
        }

        return savedEntity;
    }

    private void addMembersToGroup(Long idGrup, List<Utilizator> members){
        String sql = "INSERT INTO membri_grup (id, id_utilizator) VALUES (?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            connection.setAutoCommit(false); //Pentru a ma asigura ca se fac toate inserarile sau nici una

            try {
                for(Utilizator member : members){
                    statement.setLong(1, idGrup);
                    statement.setLong(2, member.getId());

                    statement.addBatch(); //Adaug insert-ul pentru fiecare membru
                }

                statement.executeBatch(); //Execut insertul intr-o singura operatie

                connection.commit(); //Completez tranzactia
            }
            catch (SQLException e) {
                connection.rollback(); //Pentru a nu salva datele partial
                throw new RepoException("A aparut o eroare la salvarea membrilor!");
            }
            finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RepoException("A aparut o eroare la adaugarea mebrilor!");
        }
    }


    @Override
    public Optional<E> update(E entity) {
        Optional<E> updatedEntity = super.update(entity);

        // Daca entitatea trebuie actualizata
        if(updatedEntity.isEmpty()){
            try (PreparedStatement statement = connection.prepareStatement(entityToUpdateSQL(entity))) {
                // Seteaza parametrii pentru UPDATE
                setUpdateParameters(statement, entity);

                // Executa UPDATE
                int affectedRows = statement.executeUpdate();

                // Daca nu s-au afectat randuri, inseamna ca entitatea nu a fost actualizata
                if (affectedRows == 0) {
                    throw new RepoException("Nu s-au produs modificari la BD in urma actualizarii! \n");
                }
            }
            catch (SQLException e){
                throw  new RepoException("A aparut o eroare la actualizare in baza de date! \n");
            }
        }

        return updatedEntity;
    }

    @Override
    public Optional<E> delete(ID id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        Optional<E> entity = findOne(id);
        if (entity.isEmpty()) {
            throw new RepoException("A aparut o problema la stergerea prieteniei!");
        }

        if(!entity.isEmpty()){
            if(entity.get() instanceof Utilizator){
                deletePrietenieByIdUtilizator(id);
            }

            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setLong(1, (Long) id);
                statement.executeUpdate();
            }
            catch (SQLException e){
                e.printStackTrace();
                throw  new RepoException("Eroare la stergerea din BD! \n");
            }
        }

        return entity;
    }

    private void deletePrietenieByIdUtilizator(ID id){
        String cmd = "DELETE FROM prieteni WHERE id1 = ? OR id2 = ?";

        try(PreparedStatement statement = connection.prepareStatement((cmd))){
            statement.setLong(1, (Long) id);
            statement.setLong(2, (Long) id);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
            throw  new RepoException("Eroare la stergerea prieteniei! \n");
        }
    }
}
