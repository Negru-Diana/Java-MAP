package com.example.examen_db.service;

import com.example.examen_db.domain.City;
import com.example.examen_db.domain.TrainStation;
import com.example.examen_db.repository.db.CityDBRepository;
import com.example.examen_db.repository.db.TrainStationDBRepository;

import java.util.Iterator;
import java.util.List;

public class Service {
    private CityDBRepository repoCities;
    private TrainStationDBRepository repoTrainStations;

    public Service(CityDBRepository repoCities, TrainStationDBRepository repoTrainStations) {
        this.repoCities = repoCities;
        this.repoTrainStations = repoTrainStations;
    }

    public Iterable<City> getAllCities() {
        return repoCities.findAll();
    }


    public Iterable<TrainStation> getAllTrainStations() {
        for (TrainStation t : repoTrainStations.findAll()) {
            System.out.println(t);
        }
        return repoTrainStations.findAll();
    }


    public City findCity(String cityID){
        return repoCities.findOne(cityID).orElse(null);
    }

    public String getCityNameById(String cityId) {
        City city = repoCities.findOne(cityId).orElse(null);
        return city != null ? city.getName() : null;
    }

}
