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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrietenieDBRepository extends AbstractDBRepository<Long, Prietenie> implements PagingRepository<Long, Prietenie> {

    public PrietenieDBRepository(Validator<Prietenie> validator, Connection connection, UtilizatorDBRepository utilizatorRepository) {
        super(validator, connection);
        this.utilizatorRepository = utilizatorRepository;
        load();
    }

    @Override
    protected void load() {
        //Inacrc datele din tabela "prieteni"
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM prieteni")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Prietenie prietenie = resultSetToEntity(resultSet);

                    Utilizator u1 = prietenie.getUtilizator1();
                    Utilizator u2 = prietenie.getUtilizator2();

                    Optional<Utilizator> foundU1 = utilizatorRepository.findOne(u1.getId());
                    Optional<Utilizator> foundU2 = utilizatorRepository.findOne(u2.getId());

                    // Adăugarea prietenului în lista de prieteni
                    foundU1.ifPresent(u -> {
                        u.getFriends().add(foundU2.orElseThrow(() ->
                                new RepoException("Utilizatorul cu id-ul " + u2.getId() + " nu a fost gasit pentru prietenie!")));
                    });

                    foundU2.ifPresent(u -> {
                        u.getFriends().add(foundU1.orElseThrow(() ->
                                new RepoException("Utilizatorul cu id-ul " + u1.getId() + " nu a fost gasit pentru prietenie!")));
                    });

                    // Actualizarea utilizatorilor în com.example.retea_de_socializare.repository după ce s-au adăugat prietenii
                    foundU1.ifPresent(utilizatorRepository::update);
                    foundU2.ifPresent(utilizatorRepository::update);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la încărcarea datelor din tabela prieteni", e);
        }
    }

    @Override
    public Prietenie resultSetToEntity(ResultSet resultSet) throws SQLException {
        Long idPrietenie = resultSet.getLong("id");
        Long id1 = resultSet.getLong("id1");
        Long id2 = resultSet.getLong("id2");
        // Apare T ca si separator intre data si ora
        LocalDateTime friendsfrom = resultSet.getTimestamp("friendsfrom").toLocalDateTime();

        // Obtin utilizatorii din com.example.retea_de_socializare.repository-ul de utilizatori
        Utilizator utilizator1 = utilizatorRepository.findOne(id1).orElseThrow(() ->
                new RepoException("Utilizatorul cu id-ul " + id1 + " nu a fost gasit."));
        Utilizator utilizator2 = utilizatorRepository.findOne(id2).orElseThrow(() ->
                new RepoException("Utilizatorul cu id-ul " + id2 + " nu a fost gasit."));

        Prietenie prietenie = new Prietenie(utilizator1, utilizator2, friendsfrom);
        prietenie.setId(idPrietenie);
        return prietenie;
    }

    @Override
    public String entityToInsertSQL(Prietenie entity) {
        return "INSERT INTO prieteni (id, id1, id2, friendsfrom) VALUES (?, ?, ?, ?)";
    }

    @Override
    public String entityToUpdateSQL(Prietenie entity) {
        return "UPDATE prieteni SET id1 = ?, id2 = ?, friendsfrom = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, Prietenie entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setLong(2, entity.getUtilizator1().getId());
        statement.setLong(3, entity.getUtilizator2().getId());
        statement.setTimestamp(4, Timestamp.valueOf(entity.getFriendsfrom()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, Prietenie entity) throws SQLException {
        statement.setLong(1, entity.getUtilizator1().getId());
        statement.setLong(2, entity.getUtilizator2().getId());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsfrom()));
        statement.setLong(4, entity.getId());
    }

    @Override
    protected String getTableName() {
        return "prieteni";
    }

    @Override
    public Iterable<Prietenie> findAll() {
        List<Prietenie> prietenii = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM prieteni")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Prietenie prietenie = resultSetToEntity(resultSet);
                    prietenii.add(prietenie);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la incarcarea tuturor prieteniilor din baza de date! \n");
        }
        return prietenii;
    }

    @Override
    public Optional<Prietenie> findOne(Long id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM prieteni WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Prietenie prietenie = resultSetToEntity(resultSet);
                    return Optional.of(prietenie); // Returneaza prietenia gasita
                } else {
                    return Optional.empty(); // Daca nu exista prietenie cu acel ID
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la cautarea prieteniei cu id-ul " + id, e);
        }
    }


    @Override
    public Page<Prietenie> findAllOnPage(Pageable pageable, FilterDTO filter) {
        List<Prietenie> prietenii = new ArrayList<>();
        int offset = pageable.getPageNumber()*pageable.getPageSize();
        int limit = pageable.getPageSize();

        //Construiesc interogarea SQL
        String sql = "SELECT * FROM prieteni WHERE (id1 = ? OR id2 = ?) LIMIT ? OFFSET ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu pentru a gasi prieteniile")));
            statement.setLong(2, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu pentru a gasi prieteniile")));
            statement.setInt(3, limit);
            statement.setInt(4, offset);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Prietenie prietenie = resultSetToEntity(resultSet);
                    prietenii.add(prietenie);
                }
            }

            int totalNumberOfElements = count(filter);

            return new Page<>(prietenii, totalNumberOfElements);
        }
        catch (SQLException e) {
            throw new RepoException("A aparut o problema la incarcarea prietenilor in format paginat!");
        }

    }

    public int count(FilterDTO filter) {
        String sql = "SELECT COUNT(*) FROM prieteni WHERE id1 = ? OR id2 = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu pentru a gasi prieteniile")));
            statement.setLong(2, filter.getUserId().orElseThrow(() -> new RepoException("Id-ul utilizatorului este obligatoriu pentru a gasi prieteniile")));

            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            throw new RepoException("A aparut o eroare la numararea prieteniilor!");
        }
        return 0;
    }
}
