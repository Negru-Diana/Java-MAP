package com.example.retea_de_socializare.repository.database;

import com.example.retea_de_socializare.domain.CererePrietenie;
import com.example.retea_de_socializare.domain.Grup;
import com.example.retea_de_socializare.domain.Prietenie;
import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.domain.validators.Validator;
import com.example.retea_de_socializare.exceptions.RepoException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GrupDBRepository extends AbstractDBRepository<Long, Grup>{

    public GrupDBRepository(Validator<Grup> validator, Connection connection, UtilizatorDBRepository utilizatorRepository) {
        super(validator, connection);
        this.utilizatorRepository = utilizatorRepository;
        load();
    }

    @Override
    protected void load() {
        //Incarcarea grupurilor

        //Inacrc datele din tabela "grupuri"
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM grupuri")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Grup grup = resultSetToEntity(resultSet);

                    //Dupa ce am creat grupul, incarc membrii grupului
                    Long id = grup.getId();
                    List<Utilizator> membri = loadMembbriGrup(id);
                    grup.setMembri(membri);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la încarcarea datelor din tabela grupuri.", e);
        }
    }


    private List<Utilizator> loadMembbriGrup(Long id) {
        List<Utilizator> membri = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(("SELECT id_utilizator FROM membri_grup WHERE id = ?"))){
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id_utilizator = resultSet.getLong("id_utilizator");

                    Optional<Utilizator> utilizator = utilizatorRepository.findOne(id_utilizator);
                    utilizator.ifPresent(membri::add);
                }
            }
        }
        catch (SQLException e) {
            throw  new RepoException("A aparut o eroare la incarcarea membrilor din grup!");
        }

        return  membri;
    }


    @Override
    public Grup resultSetToEntity(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");
        String numeGrup = resultSet.getString("nume");

        Grup grup = new Grup(numeGrup);
        grup.setId(id);

        return grup;
    }


    @Override
    public String entityToInsertSQL(Grup entity) {
        return "INSERT INTO grupuri (id, nume) VALUES (?, ?)";
    }

    @Override
    public String entityToUpdateSQL(Grup entity) {
        return "UPDATE grupuri SET nume = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, Grup entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setString(2,entity.getNumeGrup());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, Grup entity) throws SQLException {
        statement.setString(1,entity.getNumeGrup());
        statement.setLong(2,entity.getId());
    }

    @Override
    protected String getTableName() {
        return "grupuri";
    }

    @Override
    public Iterable<Grup> findAll() {
        List<Grup> grupuri = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM grupuri")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Grup grup = resultSetToEntity(resultSet);

                    Long id = grup.getId();
                    List<Utilizator> membri = loadMembbriGrup(id);
                    grup.setMembri(membri);

                    grupuri.add(grup);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la incarcarea tuturor grupurilor din baza de date! \n");
        }
        return grupuri;
    }

    @Override
    public Optional<Grup> findOne(Long id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM grupuri WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Grup grup = resultSetToEntity(resultSet);
                    return Optional.of(grup); // Returneaza grupul cu ID-ul dat
                } else {
                    return Optional.empty(); // Daca nu exista grup cu acel ID
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la cautarea grupului cu id-ul " + id, e);
        }
    }

    public void deleteUserFromGroups(Long userId) {
        String sql = "DELETE FROM membri_grup WHERE id_utilizator = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Eroare la ștergerea utilizatorului din grupuri cu ID-ul " + userId, e);
        }
    }


}
