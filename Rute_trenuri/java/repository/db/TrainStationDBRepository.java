package com.example.examen_db.repository.db;

import com.example.examen_db.domain.City;
import com.example.examen_db.domain.TrainStation;
import com.example.examen_db.exceptions.RepoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainStationDBRepository extends AbstractDBRepository<Integer, TrainStation>{

    public TrainStationDBRepository(Connection connection) {
        super(connection);
        load();
    }


    @Override
    public TrainStation resultSetToEntity(ResultSet resultSet) throws SQLException {
        String train_id = resultSet.getString("train_id");
        String departureCity = resultSet.getString("departure_city");
        String destinationCity = resultSet.getString("destination_city");
        Integer id = resultSet.getInt("id");

        TrainStation trainStation = new TrainStation(id, train_id, departureCity, destinationCity);
        return trainStation;
    }

    @Override
    public void save(TrainStation entity) {

    }

    @Override
    protected void load() {
        //Incarc datele din tabela "train_stations"
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM train_stations")){
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    TrainStation entity = resultSetToEntity(resultSet);
                    super.add(entity); // Adaug utilizatorul in lista
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RepoException("Eroare la incarcarea datelor din tabela train_stations\n");
        }
    }
}
