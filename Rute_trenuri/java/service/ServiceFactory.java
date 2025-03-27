package com.example.examen_db.service;

import com.example.examen_db.exceptions.ServiceException;
import com.example.examen_db.repository.db.CityDBRepository;
import com.example.examen_db.repository.db.TrainStationDBRepository;
import com.example.examen_db.utils.DBConnection;

import java.sql.Connection;

public class ServiceFactory {

    private static ObservableService service;

    private static void create(){
        try{
            Connection connection = DBConnection.getConnection();
            CityDBRepository cityDBRepository = new CityDBRepository(connection);
            TrainStationDBRepository trainStationDBRepository = new TrainStationDBRepository(connection);
            Service service2 = new Service(cityDBRepository, trainStationDBRepository);
            service = new ObservableService(service2);
        }
        catch(Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    public static ObservableService getService(){
        if(service == null){
            create();
        }
        return service;
    }

}
