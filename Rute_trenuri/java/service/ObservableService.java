package com.example.examen_db.service;

import com.example.examen_db.domain.City;
import com.example.examen_db.domain.TrainStation;
import com.example.examen_db.utils.observers.Observable;
import com.example.examen_db.utils.observers.Observer;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObservableService implements Observable {

    private Service service;
    private List<Observer> observers = new ArrayList<>();
    private Map<Pair<String, String>, Integer> clientFilters = new HashMap<>();

    public ObservableService(Service service) {
        this.service = service;
    }

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer adăugat!");
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observers.contains(observer)) {
            observers.remove(observer); // Îndepărtează observer-ul din lista de observatori
            System.out.println("Observer eliminat!");
        }
    }

    @Override
    public void notifyObservers(int cate, Pair<String, String> filterPair) {
        for (Observer observer : observers) {
            observer.update(cate, filterPair);
        }
    }

    public void addFilter(String departure, String destination) {
        Pair<String, String> filterPair = new Pair<>(departure, destination);
        clientFilters.merge(filterPair, 1, Integer::sum);

        if(clientFilters.get(filterPair) > 1) {
            notifyObservers(clientFilters.get(filterPair), filterPair);
        }
    }



    public Iterable<City> getAllCities(){
        return service.getAllCities();
    }

    public Iterable<TrainStation> getAllTrainStations(){
        return service.getAllTrainStations();
    }

    public City findCity(String cityID){
        return service.findCity(cityID);
    }

    public String getCityNameById(String cityId){
        return service.getCityNameById(cityId);
    }
}
