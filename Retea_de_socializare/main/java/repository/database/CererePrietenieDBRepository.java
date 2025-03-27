package com.example.retea_de_socializare.repository.database;

import com.example.retea_de_socializare.domain.CererePrietenie;
import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.domain.validators.Validator;
import com.example.retea_de_socializare.exceptions.RepoException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CererePrietenieDBRepository extends AbstractDBRepository<Long, CererePrietenie>{

    public CererePrietenieDBRepository(Validator<CererePrietenie> validator, Connection connection, UtilizatorDBRepository utilizatorRepository) {
        super(validator, connection);
        this.utilizatorRepository = utilizatorRepository;
        load();
    }

    @Override
    protected void load() {
        //Incarcarea cererilor de prieteniein functie de utilizatorul care este conectat!!!!!!

        //Inacrc datele din tabela "cereri"
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM cereri")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CererePrietenie cerere = resultSetToEntity(resultSet);

                    Long id1 = cerere.getUtilizator1().getId();
                    Long id2 = cerere.getUtilizator2().getId();

                    Optional<Utilizator> foundU1 = utilizatorRepository.findOne(id1);
                    Optional<Utilizator> foundU2 = utilizatorRepository.findOne(id2);

                    if (foundU1.isPresent() && foundU2.isPresent()) {
                        cerere.setUtilizator1(foundU1.get());
                        cerere.setUtilizator2(foundU2.get());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la încarcarea datelor din tabela cereri prietenie", e);
        }
    }


    @Override
    public CererePrietenie resultSetToEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long id1 = resultSet.getLong("id1");
        Long id2 = resultSet.getLong("id2");
        // Apare T ca si separator intre data si ora
        LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
        String status = resultSet.getString("status");

        // Obtin utilizatorii din com.example.retea_de_socializare.repository-ul de utilizatori
        Utilizator utilizator1 = utilizatorRepository.findOne(id1).orElseThrow(() ->
                new RepoException("Utilizatorul cu id-ul " + id1 + " nu a fost gasit."));
        Utilizator utilizator2 = utilizatorRepository.findOne(id2).orElseThrow(() ->
                new RepoException("Utilizatorul cu id-ul " + id2 + " nu a fost gasit."));

        CererePrietenie cerere = new CererePrietenie(utilizator1, utilizator2,data,status);
        cerere.setId(id);
        return cerere;
    }

    @Override
    public String entityToInsertSQL(CererePrietenie entity) {
        return "INSERT INTO cereri (id, id1, id2, data, status) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    public String entityToUpdateSQL(CererePrietenie entity) {
        return "UPDATE cereri SET id1 = ?, id2 = ?, data = ?, status = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, CererePrietenie entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setLong(2, entity.getUtilizator1().getId());
        statement.setLong(3, entity.getUtilizator2().getId());
        statement.setTimestamp(4, Timestamp.valueOf(entity.getData()));
        statement.setString(5, entity.getStatus());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, CererePrietenie entity) throws SQLException {
        statement.setLong(1, entity.getUtilizator1().getId());
        statement.setLong(2, entity.getUtilizator2().getId());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getData()));
        statement.setString(4, entity.getStatus());
        statement.setLong(5, entity.getId());
    }

    @Override
    protected String getTableName() {
        return "cereri";
    }

    @Override
    public Iterable<CererePrietenie> findAll() {
        List<CererePrietenie> cereri = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM cereri")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CererePrietenie cerere = resultSetToEntity(resultSet);
                    cereri.add(cerere);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la incarcarea tuturor cererilor din baza de date! \n");
        }
        return cereri;
    }

    @Override
    public Optional<CererePrietenie> findOne(Long id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM cereri WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    CererePrietenie cerere = resultSetToEntity(resultSet);
                    return Optional.of(cerere); // Returneaza cererea de prietenie cu ID-ul dat
                } else {
                    return Optional.empty(); // Daca nu exista cererea cu acel ID
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la cautarea cererii cu id-ul " + id, e);
        }
    }

}
