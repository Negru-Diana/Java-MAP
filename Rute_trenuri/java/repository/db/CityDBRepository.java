package com.example.examen_db.repository.db;

import com.example.examen_db.domain.City;
import com.example.examen_db.exceptions.RepoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityDBRepository extends AbstractDBRepository<String, City>{

    public CityDBRepository(Connection connection) {
        super(connection);
        load();
    }


    @Override
    public City resultSetToEntity(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");

        City city = new City(id, name);
        return city;
    }

    @Override
    public void save(City entity) {

    }

    @Override
    protected void load() {
        //Incarc datele din tabela "cities"
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM cities")){
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    City entity = resultSetToEntity(resultSet);
                    super.add(entity); // Adaug utilizatorul in lista
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RepoException("Eroare la incarcarea datelor din tabela cities\n");
        }
    }
}
