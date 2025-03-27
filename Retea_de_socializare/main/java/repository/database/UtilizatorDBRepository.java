package com.example.retea_de_socializare.repository.database;

import com.example.retea_de_socializare.domain.Prietenie;
import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.domain.validators.Validator;
import com.example.retea_de_socializare.exceptions.RepoException;
import com.example.retea_de_socializare.repository.paging.PagingRepository;
import com.example.retea_de_socializare.utils.dto.FilterDTO;
import com.example.retea_de_socializare.utils.paging.Page;
import com.example.retea_de_socializare.utils.paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorDBRepository extends AbstractDBRepository<Long, Utilizator> implements PagingRepository<Long, Utilizator> {

    public UtilizatorDBRepository(Validator<Utilizator> validator, Connection connection) {
        super(validator, connection);
        load();
    }

    @Override
    protected void load() {
        //Incarc datele din tabela "utilizatori"
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM utilizatori")){
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    Utilizator entity = resultSetToEntity(resultSet);
                    entities.put(entity.getId(), entity); // Adaug utilizatorul in lista
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RepoException("Eroare la incarcarea datelor din tabela utilizatori\n");
        }
    }

    @Override
    public Utilizator resultSetToEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String prenume = resultSet.getString("prenume");
        String nume = resultSet.getString("nume");
        String username = resultSet.getString("username");
        String parola = resultSet.getString("parola");

        Utilizator ut = new Utilizator(prenume, nume, username, parola);
        ut.setId(id);
        return ut;
    }

    @Override
    public String entityToInsertSQL(Utilizator entity) {
        return "INSERT INTO utilizatori(id, prenume, nume, username, parola) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    public String entityToUpdateSQL(Utilizator entity) {
        return "UPDATE utilizatori SET prenume = ?, nume = ?, username = ?, parola = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, Utilizator entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getFirstName());
        statement.setString(3, entity.getLastName());
        statement.setString(4, entity.getUserName());
        statement.setString(5, entity.getParola());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, Utilizator entity) throws SQLException {
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
        statement.setString(3, entity.getUserName());
        statement.setString(4, entity.getParola());
        statement.setLong(5, entity.getId());
    }

    @Override
    protected String getTableName() {
        return "utilizatori";
    }

    @Override
    public Iterable<Utilizator> findAll() {
        List<Utilizator> utilizatori = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM utilizatori")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Utilizator utilizator = resultSetToEntity(resultSet);
                    utilizatori.add(utilizator);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la incarcarea tuturor utilizatorilor din baza de date! \n");
        }
        return utilizatori;
    }

    @Override
    public Optional<Utilizator> findOne(Long id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM utilizatori WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Utilizator utilizator = resultSetToEntity(resultSet);
                    return Optional.of(utilizator); // Returneaza utilizatorul gasit
                } else {
                    return Optional.empty(); // Daca nu exista utilizator cu acel ID
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la cautarea utilizatorului cu id-ul " + id, e);
        }
    }

    @Override
    public Page<Utilizator> findAllOnPage(Pageable pageable, FilterDTO filter) {
        System.out.println(filter.getSearchText().orElse("nope"));
        List<Utilizator> utilizatori = new ArrayList<>();
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();
        String sql;
        if(filter.getSearchText().equals(Optional.empty()))
        {
            // SQL pentru a obține utilizatori care nu sunt prieteni cu utilizatorul curent
            sql = "SELECT u.* FROM utilizatori u " +
                    "LEFT JOIN prieteni p " +
                    "  ON (p.id1 = u.id AND p.id2 = ?) " +
                    "  OR (p.id1 = ? AND p.id2 = u.id) " +
                    "WHERE u.id != ? " +  // Exclude utilizatorul curent
                    "  AND u.id != 0 " +  // Exclude utilizatorii cu ID 0
                    "  AND p.id1 IS NULL " +  // Exclude utilizatorii care sunt deja prieteni
                    "LIMIT ? OFFSET ?"; // Paginare
        }
        else{
            sql = "SELECT u.* FROM utilizatori u " +
                    "LEFT JOIN prieteni p " +
                    "ON (p.id1 = u.id AND p.id2 = ? ) " +
                    "OR (p.id1 = ? AND p.id2 = u.id) " +
                    "WHERE u.id != ? " +
                    "AND u.id != 0 " +
                    "AND p.id1 IS NULL " +
                    "AND ( " +
                    "LOWER(u.prenume || ' ' || u.nume) LIKE ? " +
                    "OR LOWER(u.nume || ' ' || u.prenume) LIKE ? " +
                    "OR LOWER(u.username) LIKE ? " +
                    " ) " +
                    "LIMIT ? OFFSET ?; ";

        }



        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if(filter.getSearchText().equals(Optional.empty())){
                statement.setLong(1, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setLong(2, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setLong(3, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setInt(4, limit);
                statement.setInt(5, offset);
            }
            else{
                statement.setLong(1, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setLong(2, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setLong(3, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));

                String searchText = "%" + filter.getSearchText().orElse("").toLowerCase() + "%"; // Adaugă wildcard pentru LIKE
                statement.setString(4, searchText); // Pentru condiția "prenume || ' ' || nume"
                statement.setString(5, searchText); // Pentru condiția "nume || ' ' || prenume"
                statement.setString(6, searchText); // Pentru condiția "username"

                statement.setInt(7, limit); // Paginare: limită
                statement.setInt(8, offset); // Paginare: offset
            }


            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Utilizator utilizator = resultSetToEntity(resultSet);
                    utilizatori.add(utilizator);
                }
            }

            int totalNumberOfElements = count(filter);
            return new Page<>(utilizatori, totalNumberOfElements);
        } catch (SQLException e) {
            throw new RepoException("A aparut o problema la incarcarea utilizatorilor.");
        }
    }

    public int count(FilterDTO filter) {
        String sql;
        if(filter.getSearchText().equals(Optional.empty())){
            sql = "SELECT COUNT(*) FROM utilizatori u " +
                    "WHERE u.id != ? " +
                    "  AND u.id != 0 " +  // Exclude utilizatorii cu ID 0
                    "  AND NOT EXISTS ( " +
                    "    SELECT 1 FROM prieteni p " +
                    "    WHERE (p.id1 = u.id AND p.id2 = ?) " +
                    "       OR (p.id1 = ? AND p.id2 = u.id)" +
                    ")";
        }
        else{
            sql = "SELECT COUNT(*) FROM utilizatori u " +
                    "LEFT JOIN prieteni p " +
                    "ON (p.id1 = u.id AND p.id2 = ? ) " +
                    "OR (p.id1 = ? AND p.id2 = u.id) " +
                    "WHERE u.id != ? " +
                    "AND u.id != 0 " +
                    "AND p.id1 IS NULL " +
                    "AND ( " +
                    "LOWER(u.prenume || ' ' || u.nume) LIKE ? " +
                    "OR LOWER(u.nume || ' ' || u.prenume) LIKE ? " +
                    "OR LOWER(u.username) LIKE ? " +
                    " ) ";
        }


        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if(filter.getSearchText().equals(Optional.empty())){
                statement.setLong(1, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setLong(2, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setLong(3, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));

            }
            else{
                statement.setLong(1, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setLong(2, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));
                statement.setLong(3, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu")));

                String searchText = "%" + filter.getSearchText().orElse("").toLowerCase() + "%";
                statement.setString(4, searchText); // Prenume + Nume
                statement.setString(5, searchText); // Nume + Prenume
                statement.setString(6, searchText); // Username
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("A aparut o eroare la numararea utilizatorilor.");
        }
        return 0;
    }

}
